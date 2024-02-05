package org.hstack.vmeta.extraction.scene;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.hstack.vmeta.extraction.scene.narrative.Narrative;
import org.hstack.vmeta.extraction.scene.presentation.Presentation;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.nio.file.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@Service
public class SceneExtractionService implements Runnable {


    private static final int CHANGE_DETECT_VALUE = 40;

    private String filePath;
    private SceneDTO sceneDTO;

    @Value("${fastapi.ip}")
    private String API_IP;

    @Value("${fastapi.port}")
    private String API_PORT;


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
    public SceneDTO extractSceneDTO() {

        try {
            // 장면 추출
            String dirPath = extractFrameImage(filePath);
            if (dirPath == null) {
                throw new RuntimeException();
            }

            // 이미지 타입 결정
            JsonNode imageTypeCnt = executeImageDetection(dirPath);
            int N = imageTypeCnt.findValue("N").intValue();
            int L = imageTypeCnt.findValue("L").intValue();
            int A = imageTypeCnt.findValue("A").intValue();
            int P = imageTypeCnt.findValue("P").intValue();

            // return val
            Narrative narrative = ( (N + L + P) / 3 > A )
                    ? Narrative.DESCRIPTION
                    : Narrative.APPLICATION;
            Presentation presentation = ( (N + L) > (A + P) )
                    ? Presentation.STATIC
                    : Presentation.DYNAMIC;


            return sceneDTO = SceneDTO.builder()
                    .narrative(narrative)
                    .presentation(presentation)
                    .build();

        } catch (InputMismatchException ie) {
            // TODO : Logging
            ie.printStackTrace();
        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        }

        return sceneDTO = null;
    }

    /*
     * [extractFrameImage]
     *  > 프레임 이미지 추출
     *
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - flag : 성공 여부
     */
    String extractFrameImage(String filePath) {
        try {
            // img 파일을 저장할 폴더 생성
            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "frames";

            File frameDir = new File(dirPath);
            if (frameDir.exists()) {                    // 이미 해당 폴더가 있으면
                FileUtils.cleanDirectory(frameDir);     // 삭제 후 재생성
                frameDir.delete();
            }
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
            Mat pframe = new Mat(height, width, CvType.CV_8UC3);     // prev frame
            Mat cframe = new Mat(height, width, CvType.CV_8UC3);     // curr frame

            while(true) {

                videoCapture.read(cframe);  // 영상에서 프레임 하나를 읽어 frame에 저장

                if (cframe.empty() || pframe.empty()) { // 1. 만약 더 이상 불러올 프레임이 없는 경우
                    break;                              // 종료
                } else if (frameSec == 0) {             // 2. 첫 프레임인 경우
                    OpencvCalculator.saveImage(cframe, dirPath, 0);     // 해당 이미지 저장
                    cframe.copyTo(pframe);  // curr frame 과거 처리
                } else {                                // 3. 그 외의 경우 : 프레임비교
                    double psnr = OpencvCalculator.getPSNR(pframe, cframe);
                    if (0 < psnr && psnr < CHANGE_DETECT_VALUE) {               // psnr이 기준 이하이면
                        OpencvCalculator.saveImage(cframe, dirPath, frameSec);  // 해당 이미지 저장
                        cframe.copyTo(pframe);                                  // curr frame 과거 처리
                    }
                }

                frameSec++;             // 프레임번호 추가

                for (int i = 0; i < fps - 1; i++) {
                    videoCapture.read(cframe);
                }
            }

            return dirPath;

        } catch (FileAlreadyExistsException fe) {   // 해당 폴더가 이미 있는 경우
            // TODO : logging
            fe.printStackTrace();
        } catch (NoSuchFileException ne) {          // 해당 경로가 존재하지 않는 경우
            // TODO : logging
            ne.printStackTrace();
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        }
        return null;
    }

    /*
     * [executeImageDetection]
     *  > 추출된 이미지의 타입 결정 (FastAPI 연계)
     *
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - res : 결과 JSON
     */
    JsonNode executeImageDetection(String filePath) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://" + API_IP + ":" + API_PORT)
                .build();

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("data", filePath);

        JsonNode res = webClient.post()
                .uri("/imageDetection")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonData))
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode jsonNode = mapper.readTree(s);
                        return jsonNode;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .block();

        // TODO : logging
//        System.out.println(res.findValue("N")); // NEWS
//        System.out.println(res.findValue("L")); // LECTURE
//        System.out.println(res.findValue("A")); // APPLICATION
//        System.out.println(res.findValue("P")); // PRESENTATION
        return res;
    }
}
