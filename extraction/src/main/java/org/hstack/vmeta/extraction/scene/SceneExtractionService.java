package org.hstack.vmeta.extraction.scene;

import org.hstack.vmeta.extraction.scene.narrative.Narrative;
import org.hstack.vmeta.extraction.scene.presentation.Presentation;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.InputMismatchException;

@Service
public class SceneExtractionService {

    private static final int CHANGE_DETECT_VALUE = 15;

    /*
     * 영상 기본 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - narrative, presentation
     */
    public SceneDTO extractSceneDTO(String filePath) {

        try {
            // return val
            Narrative narrative;
            Presentation presentation;

            return SceneDTO.builder()
                    .narrative(narrative)
                    .presentation(presentation)
                    .build();

        } catch (InputMismatchException ie) {
            // TODO : Logging
            ie.printStackTrace();
        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    /*
     * 프레임 이미지 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - flag : 성공 여부
     */
    private boolean extractFrameImage(String filePath) {
        try {
            // TODO : create dir
            String dirPath;

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(filePath);

            // 기본 정보 받기
            int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);
            int width = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            int height = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

            int frameNo = 0;
            Mat pframe = new Mat(width, height, CvType.CV_8UC3);     // prev frame
            Mat cframe = new Mat(width, height, CvType.CV_8UC3);     // curr frame

            while(true) {
                videoCapture.read(cframe);  // 영상에서 프레임 하나를 읽어 frame에 저장

                if (cframe.empty() || pframe.empty()) { // 1. 만약 더 이상 불러올 프레임이 없는 경우
                    break;                              // 종료
                } else if (frameNo == 0) {              // 2. 첫 프레임인 경우
                    OpencvCalculator.saveImage(cframe, dirPath, 0);         // 해당 이미지 저장
                } else if (frameNo % fps == 0) {        // 3. 그 외의 경우 : 1초마다
                    double psnr = OpencvCalculator.getPSNR(pframe, cframe);
                    if (0 < psnr && psnr < CHANGE_DETECT_VALUE) {                   // psnr이 기준 이하이면
                        OpencvCalculator.saveImage(cframe, dirPath, frameNo/fps);   // 해당 이미지 저장
                    }
                }

                pframe = cframe;            // curr frame 과거 처리
                frameNo++;                  // 프레임번호 추가
            }

            return true;
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
            return false;
        }
    }
}
