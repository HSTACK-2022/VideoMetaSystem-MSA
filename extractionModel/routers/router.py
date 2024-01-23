import os
from fastapi import APIRouter
from pydantic import BaseModel

from services import keywordService
from services import imageDetectionService

# param 객체 선언
class JsonData(BaseModel):
    data: str = None

# router 선언
router = APIRouter()

@router.post("/keyword", tags=['keyword'])
def getKeyword(jsonData : JsonData):
    jsonDict = jsonData.dict()
    return keywordService.extractKeyword(jsonDict['data'])

@router.post("/imageDetection", tags=['imageDetection'])
def getImageType(jsonData : JsonData):
    print(jsonData)
    jsonDict = jsonData.dict()
    return imageDetectionService.extractImageType(jsonDict['data'])