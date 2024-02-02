###########################################################################################
# [indexScriptClass.py]
#
# indexScriptService에서 쓰이는 class를 정의합니다.
#
###########################################################################################
# [수정내역]
# YYMMDD - MODIFIER : COMMENT
# 240131 -          : translate from VideoMetaSystem/Flask/hstack/uploadApi/indexingService.py
###########################################################################################
# [참고링크]
# https://bab2min.tistory.com/552
# https://bab2min.tistory.com/570
###########################################################################################


import os
import re
from unittest import result
import networkx

from datetime import datetime
 
class RawSentence:
    def __init__(self, textIter):
        if type(textIter) == str: self.textIter = textIter.split('\n')
        else: self.textIter = textIter
        self.rgxSplitter = re.compile('([.!?:](?:["\']|(?![0-9])))')
 
    def __iter__(self):
        for line in self.textIter:
            ch = self.rgxSplitter.split(line)
            for s in map(lambda a, b: a + b, ch[::2], ch[1::2]):
                if not s: continue
                yield s
 
class RawSentenceReader:
    def __init__(self, filepath):
        self.filepath = filepath
        self.rgxSplitter = re.compile('([.!?:](?:["\']|(?![0-9])))')
 
    def __iter__(self):
        for line in open(self.filepath, encoding='utf-8'):
            ch = self.rgxSplitter.split(line)
            for s in map(lambda a, b: a + b, ch[::2], ch[1::2]):
                if not s: continue
                yield s
 
class RawTagger:
    def __init__(self, textIter, tagger = None):
        if tagger:
            self.tagger = tagger
        else :
            from konlpy.tag import Komoran
            self.tagger = Komoran()
        if type(textIter) == str: self.textIter = textIter.split('\n')
        else: self.textIter = textIter
        self.rgxSplitter = re.compile('([.!?:](?:["\']|(?![0-9])))')
 
    def __iter__(self):
        for line in self.textIter:
            ch = self.rgxSplitter.split(line)
            for s in map(lambda a,b:a+b, ch[::2], ch[1::2]):
                if not s: continue
                yield self.tagger.pos(s)
 
class RawTaggerReader:
    def __init__(self, filepath, tagger = None):
        if tagger:
            self.tagger = tagger
        else :
            from konlpy.tag import Komoran
            self.tagger = Komoran()
        self.filepath = filepath
        self.rgxSplitter = re.compile('([.!?:](?:["\']|(?![0-9])))')
 
    def __iter__(self):
        for line in open(self.filepath, encoding='utf-8'):
            ch = self.rgxSplitter.split(line)
            for s in map(lambda a,b:a+b, ch[::2], ch[1::2]):
                if not s: continue
                yield self.tagger.pos(s)

class TextRank:
    def __init__(self, **kargs):
        self.graph = None
        self.window = kargs.get('window', 5)
        self.coef = kargs.get('coef', 1.0)
        self.threshold = kargs.get('threshold', 0.01)
        self.dictCount = {}
        self.dictBiCount = {}
        self.dictNear = {}
        self.nTotal = 0
 
 
    def load(self, sentenceIter, wordFilter = None):
        def insertPair(a, b):
            if a > b: a, b = b, a
            elif a == b: return
            self.dictBiCount[a, b] = self.dictBiCount.get((a, b), 0) + 1
 
        def insertNearPair(a, b):
            self.dictNear[a, b] = self.dictNear.get((a, b), 0) + 1
 
        for sent in sentenceIter:
            for i, word in enumerate(sent):
                if wordFilter and not wordFilter(word): continue
                self.dictCount[word] = self.dictCount.get(word, 0) + 1
                self.nTotal += 1
                if i - 1 >= 0 and (not wordFilter or wordFilter(sent[i-1])): insertNearPair(sent[i-1], word)
                if i + 1 < len(sent) and (not wordFilter or wordFilter(sent[i+1])): insertNearPair(word, sent[i+1])
                for j in range(i+1, min(i+self.window+1, len(sent))):
                    if wordFilter and not wordFilter(sent[j]): continue
                    if sent[j] != word: insertPair(word, sent[j])
 
    def loadSents(self, sentenceIter, tokenizer = None):
        #RawSentenceReader('test7.txt'), lambda sent: filter(lambda x:x 'NNP', 'VV', 'VA'), tagger.pos(sent)
        import math
        def similarity(a, b):
            n = len(a.intersection(b))
            return n / float(len(a) + len(b) - n) / (math.log(len(a)+1) * math.log(len(b)+1))
 
        if not tokenizer: rgxSplitter = re.compile('[\\s.,:;-?!()"\']+')
        
        #~~~~~~~~~~~~~~~단어 넣기 수정~~~~~~~~~~~~~~~~~
        #인풋에 토큰들의 리스트의 리스트를 넣어주면 됩니다. 인풋은 결국 문장의 iterator로 처리되기 때문에, 문장의 리스트를 넣어도 작동하거든요.
        #토큰들의 리스트가 문장이라고 볼수 있기 때문에 토큰들의 리스트의 리스트를 넣어주시면 되겠습니다.
        
        sentSet = []
        #print(sentenceIter)
        for sent in filter(None, sentenceIter):
            if type(sent) == str:
                if tokenizer: s = set(filter(None, tokenizer(sent)))
                else: s = set(filter(None, rgxSplitter.split(sent)))  
            else: s = set(sent)
            if len(s) < 2: continue
            self.dictCount[len(self.dictCount)] = sent
            sentSet.append(s)
            #sentSet.append(sentSet2)

        #각 문장을 정점으로하는 그래프를 생성
        #sentSet 내의 모든 두 쌍의 문장(sentSet[i]와 sentSet[j])에 대해 유사도를 계산하고, 
        #이 값이 특정한 threshold보다 높을 경우 i-j의 연결강도를 s로 설정

        for i in range(len(self.dictCount)):
            for j in range(i+1, len(self.dictCount)):
                s = similarity(sentSet[i], sentSet[j])
                if s < self.threshold: continue
                self.dictBiCount[i, j] = s
        #print("$$$$$$$$$$$$$$$$$$$$$$$")
        #print(self.dictCount)
   
 
    def getPMI(self, a, b):
        import math
        co = self.dictNear.get((a, b), 0)
        if not co: return None
        return math.log(float(co) * self.nTotal / self.dictCount[a] / self.dictCount[b])
 
    def getI(self, a):
        import math
        if a not in self.dictCount: return None
        return math.log(self.nTotal / self.dictCount[a])
 
    def build(self):
        self.graph = networkx.Graph()
        self.graph.add_nodes_from(self.dictCount.keys())
        for (a, b), n in self.dictBiCount.items():
            self.graph.add_edge(a, b, weight=n*self.coef + (1-self.coef))
 
    def rank(self):
        return networkx.pagerank(self.graph, weight='weight')
 
    def extract(self, ratio = 0.1):
        ranks = self.rank()
        #cand = sorted(ranks, key=ranks.get, reverse=True)[:int(len(ranks) * ratio)]
        cand = ['기능', '메모리', '세', '역할', '여러분', '것', '요청', '정도', '바로', '구조', '운영', '여기', '생각', '관리', '사용', '대표', '가지', '커널', '리소스', '체제', '형태', '프로', '문제', '입출력', '핵심', '한번', '뭔가', '실행', '하나', '정리', '우리', '파일', '그림', '단일', '자원']
        
        pairness = {}
        startOf = {}
        tuples = {}

        #cand 리스트에 있는 단어 목록들을 순회 
        #cand에는 텍스트 랭크를 기반으로 추출한 상위 N개의 키워드가 들어가 있
        for k in cand:
          
            tuples[(k,)] = self.getI(k) * ranks[k]
            #이 상위 N개의 키워드 간의 PMI를 계산하여 pairness에 저장
            #pairness의 key는 (키워드1, 키워드2)이고, value는 PMI값
            for l in cand:
                if k == l: continue
                pmi = self.getPMI(k, l)
                if pmi: pairness[k, l] = pmi
 
        for (k, l) in sorted(pairness, key=pairness.get, reverse=True):
            print(k[0], l[0] , pairness[k, l])
            if k not in startOf: startOf[k] = (k, l)
 
        for (k, l), v in pairness.items():
            pmis = v
            rs = ranks[k] * ranks[l]
            path = (k, l)
            tuples[path] = pmis / (len(path) - 1) * rs ** (1 / len(path)) * len(path)
            last = l


            #pairness에 저장된 값들을 활용해 키워드를 연장
            #예를 들어 pairness에 (키워드1, 키워드2)도 들어있고, (키워드2, 키워드3)도 들어있다면, 
            #이들을 연결해 (키워드1, 키워드2, 키워드3) 으로 확장
            while last in startOf and len(path) < 7:
                if last in path: break
                pmis += pairness[startOf[last]]
                last = startOf[last][1]
                rs *= ranks[last]
                path += (last,)
                tuples[path] = pmis / (len(path) - 1) * rs ** (1 / len(path)) * len(path)
 
        used = set()
        both = {}
        for k in sorted(tuples, key=tuples.get, reverse=True):
            if used.intersection(set(k)): continue
            both[k] = tuples[k]
            for w in k: used.add(w)
 
        return both
 
    def summarize(self, ratio = 0.333):
        r = self.rank()
        ks = sorted(r, key=r.get, reverse=True)[:int(len(r)*ratio)]
        #print(ks)
        return '\n'.join(map(lambda k:self.dictCount[k], sorted(ks)))