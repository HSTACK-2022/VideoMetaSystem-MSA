package org.hstack.vmeta.videoMetadata.video;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    final String baseUrl = "/video";

    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("전체 비디오 조회")
    void getAllVideo() throws Exception{
        // given
        List<VideoDTO> savedVideoDTOList = new ArrayList<>();
        savedVideoDTOList.add(VideoDTO.builder()
                .id(1L)
                .title("testTitle1")
                .uploaderName("testUploader1")
                .thumbnailPath("/test/1L/path.mp4")
                .build());
        savedVideoDTOList.add(VideoDTO.builder()
                .id(2L)
                .title("testTitle2")
                .uploaderName("testUploader2")
                .thumbnailPath("/test/2L/path.mp4")
                .build());

        // mocking
        given(videoService.getAll()).willReturn(savedVideoDTOList);

        // when
        final ResultActions actions =
                mockMvc.perform(
                        get(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("비디오 저장")
    void saveVideo() throws Exception {
        // given
        VideoDTO videoDTO = VideoDTO.builder().title("testTitle").build();
        Long expectVideoId = 0L;

        // mocking
        given(videoService.save(videoDTO)).willReturn(expectVideoId);

        // when
        final ResultActions actions =
                mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(videoDTO))
                                .characterEncoding("UTF-8")
                );

        String body = "save video #" + expectVideoId + " succeed.";

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(body))
                .andDo(print());
    }

    @Test
    @DisplayName("비디오 삭제")
    void deleteVideo() throws Exception{
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


        String body = "delete video #" + videoId + " succeed.";

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(body))
                .andDo(print());
    }
}