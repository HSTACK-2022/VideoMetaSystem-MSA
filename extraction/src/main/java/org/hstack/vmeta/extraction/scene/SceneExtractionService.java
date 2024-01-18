package org.hstack.vmeta.extraction.scene;

import org.hstack.vmeta.extraction.basic.BasicDTO;
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
import java.nio.file.*;
import java.sql.Time;
import java.util.InputMismatchException;

@Service
public class SceneExtractionService implements Runnable {


    private static final int CHANGE_DETECT_VALUE = 30;

    private String filePath;
    private SceneDTO sceneDTO;

    /*
     * getter, setter
     */
    public void init(String filePath) {
        this.filePath = filePath;
    }
    public SceneDTO getResult() {
        return sceneDTO;
    }

    /*
     * 스레드를 위한 run()
     */
    @Override
    public void run() {
        extractSceneDTO();
    }


    /*
     * 영상 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - narrative, presentation
     */
    private SceneDTO extractSceneDTO() {

        try {

            extractFrameImage(filePath);        // 장면 추출
            // TODO : judge narrative, presentation by using tensorflow

            // return val
            Narrative narrative;
            Presentation presentation;

            return SceneDTO.builder()
//                    .narrative(narrative)
//                    .presentation(presentation)
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
    boolean extractFrameImage(String filePath) {
        try {
            // img 파일을 저장할 폴더 생성
            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "frames";
            Files.createDirectory(Paths.get(dirPath));

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(filePath);

            // 기본 정보 받기
            int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);
            int width = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            int height = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

            long frameSec = 0;
            Mat pframe = new Mat(width, height, CvType.CV_8UC3);     // prev frame
            Mat cframe = new Mat(width, height, CvType.CV_8UC3);     // curr frame

            while(true) {
                videoCapture.read(cframe);  // 영상에서 프레임 하나를 읽어 frame에 저장

                if (cframe.empty() || pframe.empty()) { // 1. 만약 더 이상 불러올 프레임이 없는 경우
                    break;                              // 종료
                } else if (frameSec == 0) {             // 2. 첫 프레임인 경우
                    OpencvCalculator.saveImage(cframe, dirPath, 0);     // 해당 이미지 저장
                } else if (frameSec % fps == 0) {       // 3. 그 외의 경우 : 1초마다
                    double psnr = OpencvCalculator.getPSNR(pframe, cframe);
                    if (0 < psnr && psnr < CHANGE_DETECT_VALUE) {               // psnr이 기준 이하이면
                        OpencvCalculator.saveImage(cframe, dirPath, frameSec);  // 해당 이미지 저장
                    }
                }

                pframe = cframe;                    // curr frame 과거 처리
                frameSec++;                         // 프레임번호 추가

                // 시간 단축을 위해 fps만큼의 프레임을 미리 읽는다.
                Mat m = new Mat();
                for (int i = 0; i < fps; i++) {
                    videoCapture.read(m);
                }
            }

            return true;

        } catch (FileAlreadyExistsException fe) {   // 해당 폴더가 이미 있는 경우
            // TODO : logging
            fe.printStackTrace();
        } catch (NoSuchFileException ne) {          // 해당 경로가 존재하지 않는 경우
            // TODO : logging
            ne.printStackTrace();
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        } finally {
            return false;
        }
    }
}
