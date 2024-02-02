package org.hstack.vmeta.extraction.category;

import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryExtractionService {

    @Autowired
    private CategoryCalculator categoryCalculator;

    /*
     * [extractCategoryDTO]
     *  > keywordDTO와 title을 기반으로 CategoryDTO 추출
     * @param
     * - keywordDTO : 키워드
     * - title : 영상의 제목
     * @returnVal
     * - CategoryDTO : List<Category>
     */
    public CategoryDTO extractCategoryDTO(KeywordDTO keywordDTO, String title) {
        try {

            String parsedText = keywordDTO2String(keywordDTO, title);
            String[] apiResponse = categoryCalculator.executeSTT(parsedText);
            Map<String, Float> categoryMap = categoryCalculator.parseRes2MorphMap(apiResponse);
            List<CategoryDTO.Category> category = categoryCalculator.categoryMap2List(categoryMap);

            return CategoryDTO.builder()
                    .category(category)
                    .build();

        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        }

        return null;
    }

    /*
     * [keywordDTO2String]
     *  > keywordDTO와 title을 String으로 변환
     * @param
     * - keywordDTO : 키워드
     * - title : 영상의 제목
     * @returnVal
     * - linked string val
     */
    String keywordDTO2String(KeywordDTO keywordDTO, String title) {
        
        // 빠른 연산을 위해 StringBuilder에 선 저장
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(' ');
        
        // Script 내용 저장
        List<KeywordDTO.Keyword> keywordList = keywordDTO.getKeyword();
        for (KeywordDTO.Keyword k : keywordList) {
            sb.append(k.getKeyword()).append(' ');
        }
        
        // String으로 변환하여 반환
        return sb.toString();
    }
}
