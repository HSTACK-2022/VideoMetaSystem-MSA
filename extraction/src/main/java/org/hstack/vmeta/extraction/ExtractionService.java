package org.hstack.vmeta.extraction;

import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.audio.AudioExtractionService;
import org.hstack.vmeta.extraction.basic.BasicDTO;
import org.hstack.vmeta.extraction.basic.BasicExtractionService;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.hstack.vmeta.extraction.keyword.KeywordExtractionService;
import org.hstack.vmeta.extraction.scene.SceneDTO;
import org.hstack.vmeta.extraction.scene.SceneExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ExtractionService {

    @Autowired
    private BasicExtractionService basicExtractionService;

    @Autowired
    private AudioExtractionService audioExtractionService;

    @Autowired
    private SceneExtractionService sceneExtractionService;

    @Autowired
    private KeywordExtractionService keywordExtractionService;

    public MetadataDTO extractMetadataDTO(Long id, String filePath) {
        try {

            // 파일 경로로 각 서비스 초기화
            basicExtractionService.init(filePath);
            audioExtractionService.init(filePath);
            sceneExtractionService.init(filePath);

            // 병렬 실행을 위한 스레드 생성
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            threadPool.execute(basicExtractionService);
            threadPool.execute(audioExtractionService);
            threadPool.execute(sceneExtractionService);

            // 스레드 종료 대기
            threadPool.awaitTermination(15, TimeUnit.MINUTES);

            BasicDTO basicDTO = basicExtractionService.getResult();
            AudioDTO audioDTO = audioExtractionService.getResult();
            SceneDTO sceneDTO = sceneExtractionService.getResult();

            // 키워드, 카테고리, 스크립트 순차 진행
            KeywordDTO keywordDTO = keywordExtractionService.extractKeywordDTO(audioDTO);


            return MetadataDTO.builder()
                    .id(id)
                    .length(basicDTO.getLength())
                    .videoSize(basicDTO.getVideoSize())
                    .videoType(basicDTO.getVideoType())
                    .videoFrame(basicDTO.getVideoFrame())
                    .script(audioDTO.getScript())
                    .narrative(sceneDTO.getNarrative())
                    .presentation(sceneDTO.getPresentation())
                    .keyword(keywordDTO.getKeyword())
//                    .indexScript(indexScriptDTO.getIndexScript())
//                    .category(categoryDTO.getCategory())
                    .build();

        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        }

        return null;
    }
}
