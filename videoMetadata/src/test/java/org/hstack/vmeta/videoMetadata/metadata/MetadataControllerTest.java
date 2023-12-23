package org.hstack.vmeta.videoMetadata.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.videoMetadata.video.VideoController;
import org.hstack.vmeta.videoMetadata.video.VideoDTO;
import org.hstack.vmeta.videoMetadata.video.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MetadataController.class)
class MetadataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MetadataService metadataService;

    @Autowired
    private ObjectMapper objectMapper;

    final String baseUrl = "/metadata";

    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("메타데이터 저장")
    void saveMetadata() throws Exception {
        // given
        MetadataDTO metadataDTO = MetadataDTO.builder().build();
        Long expectVideoId = 0L;

        // mocking
        given(metadataService.save(metadataDTO)).willReturn(expectVideoId);

        // when
        final ResultActions actions =
                mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(metadataDTO))
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("메타데이터 업데이트 - 키워드")
    void updateMetadata() throws Exception {
        // given
        MetadataDTO metadataDTO = MetadataDTO.builder().build();
        Long expectVideoId = 0L;

        // mocking
        given(metadataService.update(metadataDTO)).willReturn(expectVideoId);

        // when
        final ResultActions actions =
                mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(metadataDTO))
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("메타데이터 삭제")
    void deleteMetadata() throws Exception{
            // given
            Long videoId = 1L;

            // mocking

            // when
            final ResultActions actions =
                    mockMvc.perform(
                            get(baseUrl + "/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .param("videoId", String.valueOf(videoId))
                                    .characterEncoding("UTF-8")
                    );

            // then
            actions.andExpect(status().isOk())
                    .andDo(print());
    }
}