package org.hstack.vmeta.extraction.scene;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SceneExtractionServiceTest {

    @Autowired
    private SceneExtractionService sceneExtractionService;

    private static final String filePath = "E:\\test\\WEEK4_01.mp4";;

    @Test
    @DisplayName("장면 추출 전체")
    void extractSceneDTO() {
        // given
        sceneExtractionService.init(filePath);

        // when
        SceneDTO sceneDTO = sceneExtractionService.extractSceneDTO();

        // then
        assertThat(sceneDTO).isNotNull();
        assertThat(sceneDTO).isInstanceOf(SceneDTO.class);
        assertThat(sceneDTO.getNarrative()).isNotNull();
        assertThat(sceneDTO.getPresentation()).isNotNull();

        System.out.println(sceneDTO.getNarrative());
        System.out.println(sceneDTO.getPresentation());
    }

    @Test
    @DisplayName("장면 프레임 추출 - 정상실행")
    void extractFrameImage() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        // given
        String expectDirPath = Paths.get(filePath).getParent().toString()
                + File.separator
                + "frames";
        File expectDir = new File(expectDirPath);

        // when
        sceneExtractionService.extractFrameImage(filePath);

        // then
        assertTrue(expectDir.exists());
        assertTrue(expectDir.isDirectory());
        assertThat(expectDir.list().length).isNotEqualTo(0);
    }

    @Test
    @DisplayName("장면 타입 결정")
    void executeImageDetection() throws Exception {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:9001")
                .build();

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("data", "E:\\test\\frames");

        JsonNode res = webClient.post()
                .uri("/imageDetection")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonData))
                .retrieve()
                .bodyToMono(String.class)
                    .map(s -> {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            JsonNode jsonNode = mapper.readTree(s);
                            return jsonNode;
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                .block();

        assertThat(res).isNotNull();
        System.out.println(res.findValue("N")); // NEWS
        System.out.println(res.findValue("L")); // LECTURE
        System.out.println(res.findValue("A")); // APPLICATION
        System.out.println(res.findValue("P")); // PRESENTATION
    }
}