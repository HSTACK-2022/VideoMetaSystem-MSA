import uvicorn
import configparser

from fastapi import FastAPI

from routers import languageRouter

def createApp():
    app = FastAPI()
    app.include_router(languageRouter.router)
    return app


###########################################################
####                   START OF MAIN                   ####
###########################################################

app = createApp()

if __name__ == '__main__' :

    # read server setting from config file
    conf = configparser.ConfigParser()
    conf.read('config.ini', encoding='utf-8')

    # execute api
    uvicorn.run(
        'main:app'
        , host = conf['server']['host']
        , port = (int)(conf['server']['port'])
        , reload = True)

###########################################################
####                    END OF MAIN                    ####
###########################################################