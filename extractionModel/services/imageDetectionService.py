###########################################################################################
# [imageDetectionService.py]
#
# dirPath를 기반으로 imageModel.executeModel()을 호출한다.
# 호출 반환값을 기반으로 각 imageType의 개수를 반환한다.
#
###########################################################################################
# [수정내역]
# YYMMDD - MODIFIER : COMMENT
# 240122 -          : copy from VideoMetaSystem/Flask/hstack/uploadApi/imageSeperate/sceneText.py
###########################################################################################

from imageModel import imageModel

import os

###########################################################
####                   전역변수 선언                   ####
###########################################################

EXT_LIST = ['.png', '.jpg']

## [ extractImageType ]
##  - dirPath 밑에 있는 이미지를 분석한다..
## 
## @param
##  - dirPath
##    : 분석 대상 이미지가 있는 폴더의 경로
## 
## @return
##  - imageTypeCnt
# #   : 각 type별 이미지의 개수를 저장한 값
##
def extractImageType(dirPath):
    imageModel.executeModel(dirPath)
    imageDir = os.listdir(dirPath)
    imageTypeCnt = {'N' : 0
                    , 'L' : 0
                    , 'A' : 0
                    , 'P' : 0}
    
    for imageFile in imageDir:
        ext = os.path.splitext(imageFile)[1]    # 확장자 체크
        if ext in EXT_LIST:                     # 이미지 확장자인 경우에만 작업 진행
            imageType = imageFile.split('_')[0]
            imageTypeCnt[imageType] += 1

    return imageTypeCnt
