package org.hstack.vmeta.extraction.keyword;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeywordCalculator {

    @Value("${etri.keys}")
    private String[] ETRI_API_KEY;

    public List<KeywordDTO.Keyword> getKeywordList(String filePath) {

        List<KeywordDTO.Keyword> keywordList = null;

        return keywordList;
    }
}
