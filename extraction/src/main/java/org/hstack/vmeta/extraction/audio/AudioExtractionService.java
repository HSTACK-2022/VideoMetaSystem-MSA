package org.hstack.vmeta.extraction.audio;

import lombok.RequiredArgsConstructor;
import org.hstack.vmeta.extraction.audio.AudioDTO.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudioExtractionService {

    @Autowired
    private FFmpegCalculator ffmpegCalculator;

    @Autowired
    private SttCalculator sttCalculator;


    /*
     * 음성 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - script
     */
    public AudioDTO extractAudioDTO(String filePath) {
        try {
            ffmpegCalculator.videoPreprocessing(filePath);
            List<Script> script = sttCalculator.getScriptList(filePath);
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
