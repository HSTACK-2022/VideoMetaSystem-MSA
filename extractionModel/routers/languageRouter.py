import os
from fastapi import APIRouter

from services import keywordService

# router 선언
router = APIRouter(
    prefix='/language'
    , tags = ['language']
)

@router.post("/keyword")
def getKeyword(json):
    keywordDict = keywordService.extractKeyword(json['script'])
    return [{"keywords" : keywordDict}]