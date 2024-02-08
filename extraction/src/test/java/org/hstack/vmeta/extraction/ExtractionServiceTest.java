package org.hstack.vmeta.extraction;

import org.hstack.vmeta.extraction.audio.AudioExtractionService;
import org.hstack.vmeta.extraction.basic.BasicExtractionService;
import org.hstack.vmeta.extraction.category.CategoryExtractionService;
import org.hstack.vmeta.extraction.indexScript.IndexScriptExtractionService;
import org.hstack.vmeta.extraction.keyword.KeywordExtractionService;
import org.hstack.vmeta.extraction.scene.SceneExtractionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExtractionServiceTest {

    private static Long id = 1L;
    private static String title = "WEEK04_1 스레드 동기화";
    private static String filePath = "E:\\test\\WEEK4_01.mp4";

    @Autowired
    private ExtractionService extractionService;


    @Test
    @DisplayName("메타데이터 추출 전 과정")
    void extractMetadataDTO() {
        Long start = System.currentTimeMillis();
        // given

        // when
        MetadataDTO metadataDTO = extractionService.extractMetadataDTO(id, title, filePath);

        // then
        assertThat(metadataDTO).isNotNull();
        Long end = System.currentTimeMillis();
        System.out.println((start - end) / 1000);
    }
}