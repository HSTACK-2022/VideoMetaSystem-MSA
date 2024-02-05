package org.hstack.vmeta.extraction.indexScript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexScriptExtractionService implements Runnable {

    private AudioDTO audioDTO;
    private IndexScriptDTO indexScriptDTO;

    @Value("${fastapi.ip}")
    private String API_IP;

    @Value("${fastapi.port}")
    private String API_PORT;

    /*
     * getter, setter
     */
    public void init(AudioDTO audioDTO) {
        this.audioDTO = audioDTO;
    }
    public IndexScriptDTO getResult() {
        return indexScriptDTO;
    }

    /*
     * 스레드를 위한 run()
     */
    @Override
    public void run() {
        extractIndexScriptDTO();
    }


    /*
     * [extractIndexScriptDTO]
     *  > audioDTO를 기반으로 KeywordDTO 추출
     * @param
     * - audioDTO : 스크립트가 저장된 audioDTO
     * @returnVal
     * - KeywordDTO : List<keyword>
     */
    public IndexScriptDTO extractIndexScriptDTO() {
        try {
            // script -> string list로 변환
            Map<String, String> scriptMap = script2StringMap(audioDTO);

            // 키워드 목록 얻기
            Map<String, String> indexScriptMap = executeIndexScriptApi(scriptMap);

            // Map -> indexScript list로 변환
            List<IndexScriptDTO.IndexScript> indexScript = indexScriptMap2List(indexScriptMap);

            return indexScriptDTO = IndexScriptDTO.builder()
                    .indexScript(indexScript)
                    .build();

        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        }

        return indexScriptDTO = null;
    }

    /*
     * [script2StringList]
     *  > Script를 Map<String, String>으로 변환
     *
     * @param
     * - audioDTO : 추출된 audioDTO
     * @returnVal
     * - scriptMap : 변환된 Map<String, String>
     */
    Map<String, String> script2StringMap(AudioDTO audioDTO) {
        Map<String, String> scriptMap = new HashMap<>();
        for (AudioDTO.Script s : audioDTO.getScript()) {
            scriptMap.put(s.getTime().toString(), s.getContent());
        }
        return scriptMap;
    }


    /*
     * [indexScriptMap2List]
     *  > indexScriptMap을 List로 변환
     *
     * @param
     * - indexScriptMap : API로 받은 결과값
     * @returnVal
     * - indexScriptList : 변환된 List<IndexScript>
     */
    List<IndexScriptDTO.IndexScript> indexScriptMap2List(Map<String, String> indexScriptMap) {
        List<IndexScriptDTO.IndexScript> indexScriptList = new ArrayList<>();
        for (String key : indexScriptMap.keySet()) {
            indexScriptList.add(IndexScriptDTO.IndexScript
                    .builder()
                    .time(Time.valueOf(key))
                    .content(indexScriptMap.get(key))
                    .autocreated(true)
                    .expose(true)
                    .build());
        }
        return indexScriptList;
    }


    /*
     * [executeIndexScriptApi]
     *  > 주요 문장 추출 (FastAPI 연계)
     *
     * @param
     * - script : List<Script>으로 변환된 문자열
     * @returnVal
     * - res : 결과 Map (Keyword, perc)
     */
    Map<String, String> executeIndexScriptApi(Map<String, String> scriptMap) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://" + API_IP + ":" + API_PORT)
                .build();

        Map<String, Map<String, String>> jsonData = new HashMap<>();
        jsonData.put("data", scriptMap);

        Map<String, String> res = webClient.post()
                .uri("/indexScript")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonData))
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode jsonNode = mapper.readTree(s);
                        Map<String, String> keywordMap = mapper.treeToValue(jsonNode, Map.class);
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
