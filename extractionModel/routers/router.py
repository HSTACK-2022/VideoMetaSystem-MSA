import os
from fastapi import APIRouter
from pydantic import BaseModel

from services import keywordService
from services import imageDetectionService
from services import indexScriptService

# param 객체 선언
class ImageJsonData(BaseModel):
    data: str = None

class KeywordJsonData(BaseModel):
    data: list = {}

class IndexScriptJsonData(BaseModel):
    data: dict = {}


# router 선언
router = APIRouter()

@router.post("/imageDetection", tags=['imageDetection'])
def getImageType(jsonData : ImageJsonData):
    print(jsonData)
    jsonDict = jsonData.dict()
    return imageDetectionService.extractImageType(jsonDict['data'])

@router.post("/keyword", tags=['keyword'])
def getKeyword(jsonData : KeywordJsonData):
    jsonDict = jsonData.dict()
    return keywordService.extractKeyword(jsonDict['data'])

@router.post("/indexScript", tags=['indexScript'])
def getKeyword(jsonData : IndexScriptJsonData):
    jsonDict = jsonData.dict()
    return indexScriptService.extractIndexScript(jsonDict['data'])