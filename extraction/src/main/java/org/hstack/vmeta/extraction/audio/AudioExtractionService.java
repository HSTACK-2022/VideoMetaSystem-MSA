package org.hstack.vmeta.extraction.audio;

import org.hstack.vmeta.extraction.audio.AudioDTO.Script;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudioExtractionService {

    /*
     * 음성 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - script
     */
    public AudioDTO extractAudioDTO(String filePath) {

        try {

            FFmpegCalculator.videoPreprocessing(filePath);
            // TODO : call STT Service

            // return val
            List<Script> script;

            return AudioDTO.builder()
//                    .script(script)
                    .build();

        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        } finally {
            return null;
        }
    }


}
