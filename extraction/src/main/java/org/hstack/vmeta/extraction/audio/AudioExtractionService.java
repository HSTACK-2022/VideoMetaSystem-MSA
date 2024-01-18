package org.hstack.vmeta.extraction.audio;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.hstack.vmeta.extraction.audio.AudioDTO.Script;
import org.hstack.vmeta.extraction.basic.BasicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AudioExtractionService implements Runnable {


    @Autowired
    private FFmpegCalculator ffmpegCalculator;

    @Autowired
    private SttCalculator sttCalculator;


    private String filePath;
    private AudioDTO audioDTO;

    /*
     * getter, setter
     */
    public void init(String filePath) {
        this.filePath = filePath;
    }
    public AudioDTO getResult() {
        return audioDTO;
    }

    /*
     * 스레드를 위한 run()
     */
    @Override
    public void run() {
        extractAudioDTO();
    }


    /*
     * 음성 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - script
     */
    private AudioDTO extractAudioDTO() {
        try {

            // make audio dir
            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "audio";

            File audioDir = new File(dirPath);
            if (audioDir.exists()) {                    // 이미 해당 폴더가 있으면
                FileUtils.cleanDirectory(audioDir);     // 삭제 후 재생성
                audioDir.delete();
            }
            Files.createDirectory(Paths.get(dirPath));

            // AudioService 추출
            ffmpegCalculator.videoPreprocessing(filePath);
            List<Script> script = sttCalculator.getScriptList(filePath);
            
            // 메타데이터 추출 후 하위 폴더와 파일 모두 삭제
            FileUtils.cleanDirectory(audioDir);
            
            return AudioDTO.builder()
                    .script(script)
                    .build();

        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
            return null;
        }
    }


}
