###########################################################################################
# [keywordService.py]
#
# 문장 리스트를 받아 핵심 키워드를 추출한다.
#
###########################################################################################
# [수정내역]
# YYMMDD - MODIFIER : COMMENT
# 240119 -          : translate from VideoMetaSystem/Flask/hstack/uploadApi/keywordService.py
###########################################################################################

from tabnanny import verbose
import konlpy

from konlpy.tag import Okt
from krwordrank.word import summarize_with_keywords
from krwordrank.hangle import normalize


## [ extractKeyword ]
##  - 키워드를 추출한다.
## 
## @param
##  - script
##    : 문장 리스트
## 
## @return
##  - predKeyword
##    : 정제된 키워드를 확률과 함께 매핑한 딕셔너리
##
def extractKeyword(script):   

    if (script is None) :
        return

    # script 전처리
    textLines = []
    for line in script:
        textLines.append(line)

    predText = [
        normalize(text, english = False, number = True)
        for text in textLines
        ]
    
    # 추출 매개변수 선언
    beta = 0.85         # PageRank의 decaying factor beta
    maxIter = 10        # ???
    wordMinFreq = 2     # 단어의 최소 출현 빈도 수
    wordMaxLen = 10     # 단어의 최대 글자수
    verbose = True      # 프로그램 진행을 보일지 여부
    exceptWords = {'ERROR', '할', '단어'}   # 제외 단어

    # 키워드 추출
    extractedKeyword = summarize_with_keywords(
        predText
        , min_count = wordMinFreq
        , max_length = wordMaxLen
        , beta = beta
        , max_iter = maxIter
        , stopwords = exceptWords
        , verbose = verbose
        )
    
    # keyword 후처리
    predKeyword = postProcessing(extractedKeyword)
    print(predKeyword)
    return predKeyword


## [ postProcessing ]
##  - 추출된 Keyword를 후처리한다.
## 
## @param
##  - extractedKeyword
##    : krwordrank로 추출된 키워드
## 
## @return
##  - predKeyword
##    : 정제된 키워드를 확률과 함께 매핑한 딕셔너리
##
def postProcessing(extractedKeyword):

    okt = Okt()
    noun = 'Noun'
    alpha = 'Alpha'
    adjective = 'Adjective'

    # 각 키워드와 확률 매핑
    predKeyword = {}
    percSum = 0
    for k in extractedKeyword.keys():
        for word in okt.pos(''.join(k), join=True):
            if noun in word or alpha in word:           # 추출된 키워드가 명사인 경우
                perc = round(extractedKeyword[k], 3)    # 해당 키워드의 출현 확률 절사
                percSum += perc                         # 추후 확률 보정을 위한 sum 갱신
                predKeyword[word.split('/')[0]] = perc  # 단어와 확률 매핑

    # percSum에 의한 확률 조정
    for k in predKeyword:
        predKeyword[k] = round(predKeyword[k]/percSum, 3)
    
    return predKeyword


###########################################################
####                   START OF MAIN                   ####
###########################################################

if __name__ == '__main__' :
    
    ## TESTCASE
    script = [
        '계절이 지나가는 하늘에는'
        , '가을로 가득 차 있습니다.'
        , '나는 아무 걱정도 없이'
        , '가을 속의 별들을 다 헤일 듯합니다.'
        , '가슴속에 하나둘 새겨지는 별을'
        , '이제 다 못 헤는 것은'
        , '쉬이 아침이 오는 까닭이요'
        , '내일 밤이 남은 까닭이요'
        , '아직 나의 청춘이 다하지 않은 까닭입니다'
        , '별 하나에 추억과'
        , '별 하나에 사랑과'
        , '별 하나에 쓸쓸함과'
        , '별 하나에 동경과'
        , '별 하나에 시와'
        , '별 하나에 어머니 어머니'
    ]
    
    extractKeyword(script)

###########################################################
####                    END OF MAIN                    ####
###########################################################