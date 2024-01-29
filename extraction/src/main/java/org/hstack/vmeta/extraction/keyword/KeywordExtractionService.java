package org.hstack.vmeta.extraction.keyword;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordExtractionService {

    @Value("${fastapi.ip}")
    private String API_IP;

    @Value("${fastapi.port}")
    private String API_PORT;


    /*
     * [extractKeywordDTO]
     *  > audioDTO를 기반으로 KeywordDTO 추출
     * @param
     * - audioDTO : 스크립트가 저장된 audioDTO
     * @returnVal
     * - KeywordDTO : List<keyword>
     */
    public KeywordDTO extractKeywordDTO(AudioDTO audioDTO) {
        try {
            // script -> string list로 변환
            List<String> scriptList = script2StringList(audioDTO);

            // 키워드 목록 얻기
            Map<String, Float> keywordMap = executeKeywordApi(scriptList);

            // Map -> keyword list로 변환
            List<KeywordDTO.Keyword> keyword = keywordMap2List(keywordMap);

            return KeywordDTO.builder()
                    .keyword(keyword)
                    .build();

        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        }

        return null;
    }

    /*
     * [script2StringList]
     *  > Script를 List<String>으로 변환
     *
     * @param
     * - audioDTO : 추출된 audioDTO
     * @returnVal
     * - scriptList : 변환된 List<String>
     */
    List<String> script2StringList(AudioDTO audioDTO) {
        List<String> scriptList = new ArrayList<>();
        for (AudioDTO.Script s : audioDTO.getScript()) {
            scriptList.add(s.getContent());
        }
        return scriptList;
    }


    /*
     * [keywordMap2List]
     *  > keywordMap을 List로 변환
     *
     * @param
     * - keywordMap : API로 받은 결과값
     * @returnVal
     * - keywordList : 변환된 List<Keyword>
     */
    List<KeywordDTO.Keyword> keywordMap2List(Map<String, Float> keywordMap) {
        List<KeywordDTO.Keyword> keywordList = new ArrayList<>();
        for (String key : keywordMap.keySet()) {
            keywordList.add(KeywordDTO.Keyword
                    .builder()
                    .keyword(key)
                    .perc(Float.parseFloat(keywordMap.get(key)+""))
                    .autocreated(true)
                    .expose(true)
                    .build());
        }
        return keywordList;
    }


    /*
     * [executeKeywordApi]
     *  > 키워드 추출 (FastAPI 연계)
     *
     * @param
     * - script : List<String>으로 변환된 문자열
     * @returnVal
     * - res : 결과 Map (Keyword, perc)
     */
    Map<String, Float> executeKeywordApi(List<String> script) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://" + API_IP + ":" + API_PORT)
                .build();

        Map<String, List<String>> jsonData = new HashMap<>();
        jsonData.put("data", script);

        Map<String, Float> res = webClient.post()
                .uri("/keyword")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonData))
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode jsonNode = mapper.readTree(s);
                        Map<String, Float> keywordMap = mapper.treeToValue(jsonNode, Map.class);
                        return keywordMap;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .block();

        // TODO : logging
        for (String key : res.keySet()) {
            System.out.println(key + " : " + res.get(key));
        }

        return res;
    }
}
