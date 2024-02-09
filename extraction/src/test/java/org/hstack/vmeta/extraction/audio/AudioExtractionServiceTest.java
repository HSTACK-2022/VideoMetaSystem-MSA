package org.hstack.vmeta.extraction.audio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AudioExtractionServiceTest {

    private static final String filePath = "E:\\test\\WEEK4_01.mp4";

    @Autowired
    private AudioExtractionService audioExtractionService;

    @Test
    @DisplayName("Audio to Script 추출 로직 테스트")
    void extractAudioDTO() {
        try {
            audioExtractionService.init(filePath);
            AudioDTO audioDTO = audioExtractionService.extractAudioDTO();
            assertThat(audioDTO.getScript()).isNotNull();
            assertThat(audioDTO.getScript().size()).isNotEqualTo(0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}