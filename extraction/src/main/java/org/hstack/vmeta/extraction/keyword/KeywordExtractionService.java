package org.hstack.vmeta.extraction.keyword;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordExtractionService {

    /*
     * 음성 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - script
     */
    public KeywordDTO extractKeywordDTO(String filePath) {
        try {

            List<KeywordDTO.Keyword> keyword = KeywordCalculator.getKeywordList(filePath);

            return KeywordDTO.builder()
                    .keyword(keyword)
                    .build();

        } catch (Exception e) {
            // TODO : logging
            return null;
        }
    }
}
