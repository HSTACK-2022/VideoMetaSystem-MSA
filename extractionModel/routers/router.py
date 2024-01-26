import os
from fastapi import APIRouter
from pydantic import BaseModel

from services import keywordService
from services import imageDetectionService

# param 객체 선언
class KeywordJsonData(BaseModel):
    data: list = {}

class ImageJsonData(BaseModel):
    data: str = None


# router 선언
router = APIRouter()

@router.post("/keyword", tags=['keyword'])
def getKeyword(jsonData : KeywordJsonData):
    jsonDict = jsonData.dict()
    return keywordService.extractKeyword(jsonDict['data'])

@router.post("/imageDetection", tags=['imageDetection'])
def getImageType(jsonData : ImageJsonData):
    print(jsonData)
    jsonDict = jsonData.dict()
    return imageDetectionService.extractImageType(jsonDict['data'])