package org.hstack.vmeta.extraction.category;

import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryExtractionServiceTest {

    private static String title = "예시 제목";
    private static KeywordDTO keywordDTO;

    @Autowired
    private CategoryExtractionService categoryExtractionService;

    @BeforeEach
    void init() {
        List<KeywordDTO.Keyword> keyword = new ArrayList<>();
        keyword.add(KeywordDTO.Keyword.builder().keyword("하늘").build());
        keyword.add(KeywordDTO.Keyword.builder().keyword("땅").build());
        keyword.add(KeywordDTO.Keyword.builder().keyword("별").build());
        keyword.add(KeywordDTO.Keyword.builder().keyword("하나").build());
        keywordDTO = KeywordDTO.builder().keyword(keyword).build();
    }

    @Test
    @DisplayName("전체 과정 테스트")
    void extractKeywordDTO() {
        // given
        // BeforeEach에서 audioDTO 생성 완료

        // when
        CategoryDTO categoryDTO = categoryExtractionService.extractCategoryDTO(keywordDTO, title);

        // then
        assertThat(categoryDTO).isNotNull();
    }

}