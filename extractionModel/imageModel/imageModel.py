###########################################################################################
# [imageModel.py]
#
# dirPath 밑에 있는 이미지 파일의 type을 결정한다.
# 
# main으로 실행 시   : train() 진행 후 executeModel() 실행
# Service에서의 호출 : executeModel()만 호출 
#
###########################################################################################
# [수정내역]
# YYMMDD - MODIFIER : COMMENT
# 240122 -          : copy from VideoMetaSystem/Flask/hstack/uploadApi/imageSeperate/test.py
###########################################################################################


import os
import numpy as np
import tensorflow as tf
import matplotlib.image as mpimg
import matplotlib.pyplot as plt
from keras.preprocessing import image
from tensorflow.keras.optimizers import RMSprop
from tensorflow.keras.preprocessing.image import ImageDataGenerator


###########################################################
####                   전역변수 선언                   ####
###########################################################

EXT_LIST = ['.png', '.jpg']
MODEL_NAME = "imageModel.h5"
MODEL_PATH = os.path.join(os.path.dirname(os.path.realpath(__file__)), MODEL_NAME)

## [ executeModel ]
##  - dirPath 밑에 있는 이미지를 분석한다.
## 
## @param
##  - dirPath
##    : 분석 대상 이미지가 있는 폴더의 경로
## 
## @return
##  - none.
##
def executeModel(dirPath):

    # 모델 실행을 위한 매개변수 준비
    imageDir = os.listdir(dirPath)
    model = tf.keras.models.load_model(MODEL_PATH)

    for imageFile in imageDir:
        ext = os.path.splitext(imageFile)[1]    # 확장자 체크
        if ext in EXT_LIST:                     # 이미지 확장자인 경우에만 작업 진행
            absFilePath = os.path.join(dirPath, imageFile)
            imageType = predict(model, absFilePath)
            if imageType != "E" :
                newFileName = imageType + '_' + imageFile
                os.rename(absFilePath, os.path.join(dirPath, newFileName))


## [ createModel ]
##  - train을 위한 모델 정보를 생성한다.
## 
## @param
##  - none.
## 
## @return
##  - none.
##
def createModel():
    model = tf.keras.models.Sequential([
        tf.keras.layers.Conv2D(16, (3, 3), activation='relu', input_shape=(150, 150, 3)),
        tf.keras.layers.MaxPooling2D(2, 2),
        tf.keras.layers.Conv2D(32, (3, 3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2, 2),
        tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2, 2),
        tf.keras.layers.Flatten(),
        tf.keras.layers.Dense(units=512, activation='relu'),
        tf.keras.layers.Dense(units=4, activation='softmax')
    ])
    model.compile(optimizer='adam',
                loss=tf.losses.SparseCategoricalCrossentropy(from_logits=True),
                metrics=['accuracy'])
    return model

## [ train ]
##  - train, validation 폴더 밑의 정보를 기반으로 모델을 생성한다.
## 
## @param
##  - none.
## 
## @return
##  - none.
##
def train():
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    TRAIN_DIR = os.path.join(BASE_DIR, 'train')
    VALIDATION_DIR = os.path.join(BASE_DIR, 'validation')

    TRAIN_PPT_DIR = os.path.join(TRAIN_DIR, 'ppt')
    TRAIN_PP_DIR = os.path.join(TRAIN_DIR, 'pp')
    TRAIN_LECTURE_DIR = os.path.join(TRAIN_DIR, 'lecture')
    TRAIN_NEWS_DIR = os.path.join(TRAIN_DIR, 'news')

    VALIDATION_PPT_DIR = os.path.join(VALIDATION_DIR, 'ppt')
    VALIDATION_PP_DIR = os.path.join(VALIDATION_DIR, 'pp')
    VALIDATION_LECTURE_DIR = os.path.join(VALIDATION_DIR, 'lecture')
    VALIDATION_NEWS_DIR = os.path.join(VALIDATION_DIR, 'news')

    # check imgs
    nrows, ncols = 4,4
    pic_index = 0
    fig = plt.gcf()
    fig.set_size_inches(ncols*3, nrows*3)
    pic_index+=8

    model = createModel()
    model.summary()
    model.compile(optimizer=RMSprop(learning_rate=0.001), loss='categorical_crossentropy',metrics = ['accuracy'])

    # 이미지 전처리
    train_datagen = ImageDataGenerator(rescale = 1.0/255.)
    test_datagen = ImageDataGenerator(rescale = 1.0/255.)

    train_datagen = ImageDataGenerator( rescale = 1.0/255. )
    test_datagen  = ImageDataGenerator( rescale = 1.0/255. )

    train_generator = train_datagen.flow_from_directory(TRAIN_DIR, batch_size=20, class_mode='categorical', target_size=(150, 150))
    validation_generator =  test_datagen.flow_from_directory(VALIDATION_DIR, batch_size=20, class_mode  = 'categorical', target_size = (150, 150))

    history = model.fit(train_generator, validation_data=validation_generator, steps_per_epoch=100, epochs=50, validation_steps=50, verbose=2)

    model.save(MODEL_NAME)


###########################################################
####                   START OF MAIN                   ####
###########################################################

def predict(model, path):
    img = image.load_img(path, target_size=(150,150))
    x= image.img_to_array(img)
    x=np.expand_dims(x, axis=0)
    images = np.vstack([x])
    classes = model.predict(images,batch_size=10)
    if classes[0][0] == 1.0:    return "L"      #lecture
    elif classes[0][1] == 1.0:  return "N"      #news
    elif classes[0][2] == 1.0:  return "A"      #application
    elif classes[0][3] == 1.0:  return "P"      #ppt
    else :                      return "E"      #error


if __name__ == "__main__":
    # train()                     # main으로 호출시 먼저 모델 생성 진행
    
    import argparse, sys        # 이후 파일 분석
    parser = argparse.ArgumentParser(description="""Scene recognition script, this will load the model you trained, 
                            and perform inference on a sample you provide""")
    parser.add_argument("-f", "--file", help="The path to the file, preferred to be in image format (.jpg, .png)")
    args = parser.parse_args()
    dirs = args.file
    
    executeModel(dirs)          # 모델 기반으로 예측 진행

###########################################################
####                    END OF MAIN                    ####
###########################################################