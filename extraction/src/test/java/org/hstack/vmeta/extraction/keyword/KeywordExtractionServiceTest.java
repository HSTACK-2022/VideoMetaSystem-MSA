package org.hstack.vmeta.extraction.keyword;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KeywordExtractionServiceTest {

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
    private KeywordExtractionService keywordExtractionService;

    @BeforeEach
    void initAudioDTO() {
        List<AudioDTO.Script> scriptList = new ArrayList<>();
        for (String s : sampleText) {
            scriptList.add(AudioDTO.Script.builder()
                    .content(s)
                    .build());
        }

        audioDTO = AudioDTO.builder()
                .script(scriptList)
                .build();
    }

    @Test
    @DisplayName("Script 형식 변환 -> String")
    void script2StringList() {
        // given
        // BeforeEach에서 audioDTO 생성 완료

        // when
        List<String> stringList = keywordExtractionService.script2StringList(audioDTO);

        // then
        assertThat(stringList).isNotNull();
        assertThat(stringList.size()).isEqualTo(audioDTO.getScript().size());

        for (int i = 0; i < stringList.size(); i++) {
            assertThat(stringList.get(i))
                    .isEqualTo(audioDTO.getScript().get(i).getContent());
        }
    }

    @Test
    @DisplayName("FastAPI 연결")
    void executeKeywordApi() throws Exception {

        // Script 객체 생성
        List<String> script = new ArrayList<>();
        for (String s : sampleText) {
            script.add(s);
        }

        // HTTP 연결 생성
        WebClient webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:9001")
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
        assertThat(res).isNotNull();
        for (String key : res.keySet()) {
            System.out.println(key + " : " + res.get(key));
        }
    }

    @Test
    @DisplayName("Keyword 형식 변환 -> KeywordDTO")
    void keywordMap2List() {
        // given
        Map<String, Float> keywordMap = new HashMap<>();
        keywordMap.put("하나", 0.304F);
        keywordMap.put("가을", 0.272F);
        keywordMap.put("어머니", 0.214F);
        keywordMap.put("까닭", 0.21F);

        // when
        List<KeywordDTO.Keyword> keywordDTOList = keywordExtractionService.keywordMap2List(keywordMap);

        // then
        assertThat(keywordDTOList).isNotNull();
        assertThat(keywordDTOList.size()).isEqualTo(keywordMap.size());

        for (KeywordDTO.Keyword k : keywordDTOList) {
            assertThat(keywordMap.get(k.keyword)).isNotNull();
            assertThat(keywordMap.get(k.keyword)).isEqualTo(k.perc);
        }
    }

    @Test
    @DisplayName("전체 과정 테스트")
    void extractKeywordDTO() {
        // given
        // BeforeEach에서 audioDTO 생성 완료

        // when
        KeywordDTO keywordDTO = keywordExtractionService.extractKeywordDTO(audioDTO);

        // then
        assertThat(keywordDTO).isNotNull();
    }
}