package org.hstack.vmeta.extraction.category;

import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public class CategoryExtractionService {

    /*
     * [extractCategoryDTO]
     *  > audioDTO와 title을 기반으로 CategoryDTO 추출
     * @param
     * - audioDTO : 스크립트가 저장된 audioDTO
     * - title : 영상의 제목
     * @returnVal
     * - CategoryDTO : List<Category>
     */
    public CategoryDTO extractCategoryDTO(AudioDTO audioDTO, String title) {
        try {

            List<CategoryDTO.Category> category;

            return CategoryDTO.builder()
//                    .category(category)
                    .build();

        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        }

        return null;
    }

}
