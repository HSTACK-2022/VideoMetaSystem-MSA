package org.hstack.vmeta.extraction.indexScript;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IndexScriptExtractionServiceTest {

    private AudioDTO audioDTO;

    private static String[] sampleText = {
            "계절이 지나가는 하늘에는"
            , "가을로 가득 차 있습니다."
            , "나는 아무 걱정도 없이"
            , "가을 속의 별들을 다 헤일 듯합니다."
            , "가슴속에 하나둘 새겨지는 별을"
            , "이제 다 못 헤는 것은"
            , "쉬이 아침이 오는 까닭이요"
            , "내일 밤이 남은 까닭이요"
            , "아직 나의 청춘이 다하지 않은 까닭입니다"
            , "별 하나에 추억과"
            , "별 하나에 사랑과"
            , "별 하나에 쓸쓸함과"
            , "별 하나에 동경과"
            , "별 하나에 시와"
            , "별 하나에 어머니 어머니"
    };

    @Autowired
    private IndexScriptExtractionService indexScriptExtractionService;

    @BeforeEach
    void initAudioDTO() {
        int time = 0;
        List<AudioDTO.Script> scriptList = new ArrayList<>();
        for (String s : sampleText) {
            scriptList.add(AudioDTO.Script.builder()
                    .time(new Time(0, 0, time * 10))
                    .content(s)
                    .build());
            time++;
        }

        audioDTO = AudioDTO.builder()
                .script(scriptList)
                .build();
    }

    @Test
    @DisplayName("전체 과정 테스트")
    void extractIndexScriptDTO() {
        // given
        // BeforeEach에서 audioDTO 생성 완료

        // when
        indexScriptExtractionService.init(audioDTO);
        IndexScriptDTO indexScriptDTO = indexScriptExtractionService.extractIndexScriptDTO();

        // then
        assertThat(indexScriptDTO).isNotNull();
    }

    @Test
    @DisplayName("FastAPI 연결")
    void executeIndexScriptApi() {

        // Script 객체 생성
        int time = 0;
        Map<String, String> scriptMap = new HashMap<>();
        for (String s : sampleText) {
            scriptMap.put(new Time(0, 0, time++ * 10).toString(), s);
        }

        WebClient webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:9001")
                .build();

        Map<String, Map<String, String>> jsonData = new HashMap<>();
        //jsonData.put("data", scriptMap);

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
    }

    @Test
    @DisplayName("IndexScript 형식 변환 -> IndexScriptDTO")
    void indexScriptMap2List() {// given
        Map<String, String> indexScriptMap = new HashMap<>();
        indexScriptMap.put("00:00:10", "test sentence 01");
        indexScriptMap.put("00:03:10", "test sentence 02");

        // when
        List<IndexScriptDTO.IndexScript> indexScriptDTOList = indexScriptExtractionService.indexScriptMap2List(indexScriptMap);

        // then
        assertThat(indexScriptDTOList).isNotNull();
        assertThat(indexScriptDTOList.size()).isEqualTo(indexScriptMap.size());

        for (IndexScriptDTO.IndexScript i : indexScriptDTOList) {
            assertTrue(indexScriptMap.containsKey(i.getTime().toString()));
            assertThat(indexScriptMap.get(i.getTime().toString())).isEqualTo(i.getContent());
        }
    }

    @Test
    @DisplayName("Script 형식 변환 -> String")
    void script2StringMap() {
        // given
        // BeforeEach에서 audioDTO 생성 완료

        // when
        Map<String, String> stringMap = indexScriptExtractionService.script2StringMap(audioDTO);

        // then
        assertThat(stringMap).isNotNull();
        assertThat(stringMap.size()).isEqualTo(audioDTO.getScript().size());
    }
}