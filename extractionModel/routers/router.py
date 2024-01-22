import os
from fastapi import APIRouter

from services import keywordService
from services import imageDetectionService

# router 선언
router = APIRouter()

@router.post("/keyword", tags=['keyword'])
def getKeyword(json):
    return keywordService.extractKeyword(json['data'])

@router.post("/imageDetection", tags=['imageDetection'])
def getImageType(json):
    return imageDetectionService.extractImageType(json['data'])