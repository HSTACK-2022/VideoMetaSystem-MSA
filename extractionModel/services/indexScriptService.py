###########################################################################################
# [indexScriptService.py]
#
# 문장 리스트를 받아 핵심 키워드를 추출한다.
#
###########################################################################################
# [수정내역]
# YYMMDD - MODIFIER : COMMENT
# 240131 -          : translate from VideoMetaSystem/Flask/hstack/uploadApi/indexingService.py
###########################################################################################

import os
import random
import string

from konlpy.tag import Komoran

from indexScriptClass.indexScriptClass import TextRank
from indexScriptClass.indexScriptClass import RawSentenceReader

##--------------------------------------------------------- 
## [ fnLog ]
##  - 로그메세지를 출력한다.
##  - 추후 print문이 아닌 별도의 로깅 모듈 사용 필요
##--------------------------------------------------------- 
def fnLog(msg):
    print(msg)

##--------------------------------------------------------- 
## [ getHashString ]
##  - 10자의 랜덤 문자열을 생성하여 반환한다.
##--------------------------------------------------------- 
def getHashString():
    randomList = random.sample(string.ascii_letters + string.digits, 10)
    return ''.join(randomList)

##--------------------------------------------------------- 
## [ extractIndexScript ]
##  - script를 바탕으로 주요 문장을 추출한다.
## 
## @param
##  - scriptDict
##    : 문장 리스트 ({time, content}의 리스트 형태)
## 
## @return
##  - resultDict
##    : scriptDict중 주요 문장을 선별하여 반환
##--------------------------------------------------------- 
def extractIndexScript(scriptDict):
    
    textFilePath = scriptDict2TextFile(scriptDict)
    resultList = getIndexSentence(textFilePath)
    resultDict = filterIndexSentence(scriptDict, resultList)
    os.remove(textFilePath)
    
    return resultDict


##--------------------------------------------------------- 
## [ scriptDict2TextFile ]
##  - scriptDict 딕셔너리를 textFile에 붙여넣는다.
## 
## @param
##  - scriptDict
##    : 영상에서 추출한 음성의 텍스트화.
#       {time, content}의 List 형태
## 
## @return
##  - textFilePath
##    : 만들어진 텍스트 파일의 경로
##--------------------------------------------------------- 
def scriptDict2TextFile(scriptDict):

    # text file 생성
    fileName = getHashString() + ".txt"
    textFile = open(fileName, "w+", encoding='UTF-8-sig')

    # scriptDict 읽어오기
    for script in scriptDict:
        textFile.write(script['content'])
        textFile.write('\n')

    textFile.close()
    return os.path.abspath(fileName)


##--------------------------------------------------------- 
## [ getIndexSentence ]
##  - textFile에서 주요 문장을 추출해낸다.
## 
## @param
##  - textFilePath
##    : script가 저장된 텍스트 파일 경로
## 
## @return
##  - indexScriptList
##    : 추출된 주요 문장 리스트
##--------------------------------------------------------- 
def getIndexSentence(textFilePath):
    resultDic = {}

    # TextRank 초기화
    tr = TextRank()
    tagger = Komoran()
    stopword = set([('있', 'VV'), ('하', 'VV'), ('되', 'VV')])  # 이 단어는 제외한다

    # TextRank를 이용하여 핵심 어구 추출
    # 핵심 어구는 대명사만 넣도록 함
    #  - 'NNG', 'NNP', 'VV', 'VA'
    tr.loadSents(
        RawSentenceReader(textFilePath)
        , lambda sent: filter(
            lambda x:x not in stopword and x[1] in ('NP','NNG', 'NNP', 'VV', 'VA')
            , tagger.pos(sent)
        )
    )

    tr.build()
    ranks = tr.rank()
    extractedSentenceList = []
    for k in sorted(ranks, key=ranks.get, reverse=True)[:10]:
        fnLog("\t".join([str(k), str(ranks[k]), str(tr.dictCount[k])]))    #문장번호/ TR/ 문장내용
        extractedSentenceList.append(str(tr.dictCount[k]))

    # 추출된 문장 수 조절 
    if len(tr.summarize(0.1)) == 0:             # 문장 수 조절 시 아무 문장도 없는 경우
        indexScriptList = extractedSentenceList # 이전 추출 값을 그대로 취함
    else:                                       # 문장 수를 조절해도 문장이 남아 있는 경우
        reducedSentence = tr.summarize(0.1)     # 조절한 값을 취함
        indexScriptList = reducedSentence.split('\n')

    return indexScriptList


##--------------------------------------------------------- 
## [ filterIndexSentence ]
##  - scriptDict에서 resultDict에 해당하는 내용만을 골라 저장한다.
## 
## @param
##  - scriptDict
##    : 영상에서 추출한 음성의 텍스트화.
#       {time, content}의 List 형태
##  - resultList
##    : 추출된 주요 문장 리스트
## 
## @return
##  - indexScriptDict
##    : scriptDict과 resultList의 교집합 리스트
##--------------------------------------------------------- 
def filterIndexSentence(scriptDict, resultList):
    indexScriptDict = list()
    scriptDictLength = len(scriptDict)
    scriptDictOffset = 0
    for resultIndexScript in resultList:
        for i in range(scriptDictOffset, scriptDictLength):
            if (scriptDict[i]['content'] == resultIndexScript):
                indexScriptDict.append(scriptDict[i])
                scriptDictOffset = i + 1
                break

    return indexScriptDict
    


###########################################################
####                   START OF MAIN                   ####
###########################################################

if __name__ == '__main__' :
    
    ## TESTCASE
    script = [
        {'time' : '00:00:00',   'content' : '계절이 지나가는 하늘에는'}
        , {'time' : '00:00:10',   'content' : '가을로 가득 차 있습니다.'}
        , {'time' : '00:00:20',   'content' : '나는 아무 걱정도 없이'}
        , {'time' : '00:00:30',   'content' : '가을 속의 별들을 다 헤일 듯합니다.'}
        , {'time' : '00:00:40',   'content' : '가슴속에 하나둘 새겨지는 별을'}
        , {'time' : '00:00:50',   'content' : '이제 다 못 헤는 것은'}
        , {'time' : '00:01:00',   'content' : '쉬이 아침이 오는 까닭이요'}
        , {'time' : '00:01:10',   'content' : '내일 밤이 남은 까닭이요'}
        , {'time' : '00:01:20',   'content' : '아직 나의 청춘이 다하지 않은 까닭입니다'}
        , {'time' : '00:01:30',   'content' : '별 하나에 추억과'}
        , {'time' : '00:01:40',   'content' : '별 하나에 사랑과'}
        , {'time' : '00:01:50',   'content' : '별 하나에 쓸쓸함과'}
        , {'time' : '00:02:00',   'content' : '별 하나에 동경과'}
        , {'time' : '00:02:10',   'content' : '별 하나에 시와'}
        , {'time' : '00:02:20',   'content' : '별 하나에 어머니 어머니'}
    ]

    extractIndexScript(script)
    
###########################################################
####                    END OF MAIN                    ####
###########################################################