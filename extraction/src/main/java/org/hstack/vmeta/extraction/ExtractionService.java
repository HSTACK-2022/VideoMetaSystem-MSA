package org.hstack.vmeta.extraction;

import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.audio.AudioExtractionService;
import org.hstack.vmeta.extraction.basic.BasicDTO;
import org.hstack.vmeta.extraction.basic.BasicExtractionService;
import org.hstack.vmeta.extraction.category.CategoryDTO;
import org.hstack.vmeta.extraction.category.CategoryExtractionService;
import org.hstack.vmeta.extraction.indexScript.IndexScriptDTO;
import org.hstack.vmeta.extraction.indexScript.IndexScriptExtractionService;
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

    @Autowired
    private CategoryExtractionService categoryExtractionService;

    @Autowired
    private IndexScriptExtractionService indexScriptExtractionService;


    public MetadataDTO extractMetadataDTO(Long id, String title, String filePath) {
        try {

            // (Basic, Audio) >> (Scene, Keyword, IndexScript) >> (Category)

            // 1. (Basic, Audio)
            // 파일 경로로 각 서비스 초기화
            basicExtractionService.init(filePath);
            audioExtractionService.init(filePath);

            // 병렬 실행을 위한 스레드 생성
            ExecutorService preThreadPool = Executors.newFixedThreadPool(2);
            preThreadPool.execute(basicExtractionService);
            preThreadPool.execute(audioExtractionService);

            // 스레드 종료 대기 및 결과 얻기
            preThreadPool.awaitTermination(15, TimeUnit.MINUTES);
            BasicDTO basicDTO = basicExtractionService.getResult();
            AudioDTO audioDTO = audioExtractionService.getResult();


            // 2. (Scene, Keyword, Category, IndexScript)
            // 각 서비스 초기화
            sceneExtractionService.init(filePath);
            keywordExtractionService.init(audioDTO);
            indexScriptExtractionService.init(audioDTO);

            // 병렬 실행을 위한 스레드 생성
            ExecutorService postThreadPool = Executors.newFixedThreadPool(3);
            postThreadPool.execute(sceneExtractionService);
            postThreadPool.execute(keywordExtractionService);
            postThreadPool.execute(indexScriptExtractionService);

            // 스레드 종료 대기 및 결과 얻기
            postThreadPool.awaitTermination(15, TimeUnit.MINUTES);
            SceneDTO sceneDTO = sceneExtractionService.getResult();
            KeywordDTO keywordDTO = keywordExtractionService.getResult();
            IndexScriptDTO indexScriptDTO = indexScriptExtractionService.getResult();


            // 3. Category
            CategoryDTO categoryDTO = categoryExtractionService.extractCategoryDTO(keywordDTO, title);


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
                    .category(categoryDTO.getCategory())
                    .indexScript(indexScriptDTO.getIndexScript())
                    .build();

        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        }

        return null;
    }
}
