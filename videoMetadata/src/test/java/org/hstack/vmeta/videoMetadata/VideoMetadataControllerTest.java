package org.hstack.vmeta.videoMetadata;

import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(VideoMetadataController.class)
class VideoMetadataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VideoMetadataService videoMetadataService;

    final String baseUrl = "/videoMetadata";


    @Test
    @DisplayName("필터링된 비디오 조회 - 제목")
    void getFilteredVideoMetadataTitle() throws Exception {
        // given
        List<VideoMetadataDTO> savedVideoMetadataDTOList = new ArrayList<>();
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(1L)
                .title("testTitle1")
                .build());
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(2L)
                .title("testTitle2")
                .build());

        // mocking
        given(videoMetadataService.getByFilter("test", "", "", ""))
                .willReturn(savedVideoMetadataDTOList);

        // when
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("title", "test");
        paramMap.add("uploaderName", "");
        paramMap.add("keyword", "");
        paramMap.add("categoryType", "");

        final ResultActions actions =
                mockMvc.perform(
                        get(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .params(paramMap)
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("필터링된 비디오 조회 - 업로더")
    void getFilteredVideoMetadataUploaderName() throws Exception {
        // given
        List<VideoMetadataDTO> savedVideoMetadataDTOList = new ArrayList<>();
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(1L)
                .uploaderName("testUploader")
                .build());
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(2L)
                .uploaderName("testUploader")
                .build());

        // mocking
        given(videoMetadataService.getByFilter("", "test", "", ""))
                .willReturn(savedVideoMetadataDTOList);

        // when
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("title", "");
        paramMap.add("uploaderName", "test");
        paramMap.add("keyword", "");
        paramMap.add("categoryType", "");

        final ResultActions actions =
                mockMvc.perform(
                        get(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .params(paramMap)
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("필터링된 비디오 조회 - 키워드")
    void getFilteredVideoMetadataKeyword() throws Exception {
        // given
        List<Keyword> keywordList = new ArrayList<>();
        keywordList.add(Keyword.builder().mid(1L).keyword("testKeyword").build());
        keywordList.add(Keyword.builder().mid(2L).keyword("testKeyword").build());

        List<VideoMetadataDTO> savedVideoMetadataDTOList = new ArrayList<>();
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(1L)
                .keyword(keywordList)
                .build());
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(2L)
                .keyword(keywordList)
                .build());

        // mocking
        given(videoMetadataService.getByFilter("", "", "test", ""))
                .willReturn(savedVideoMetadataDTOList);

        // when
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("title", "");
        paramMap.add("uploaderName", "");
        paramMap.add("keyword", "test");
        paramMap.add("categoryType", "");

        final ResultActions actions =
                mockMvc.perform(
                        get(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .params(paramMap)
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());

    }

    @Test
    @DisplayName("필터링된 비디오 조회 - 카테고리")
    void getFilteredVideoMetadataCategory() throws Exception {
        // given
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.builder().categoryType(CategoryType.IT).build());
        categoryList.add(Category.builder().categoryType(CategoryType.IT).build());

        List<VideoMetadataDTO> savedVideoMetadataDTOList = new ArrayList<>();
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(1L)
                .category(categoryList)
                .build());
        savedVideoMetadataDTOList.add(VideoMetadataDTO.builder()
                .id(2L)
                .category(categoryList)
                .build());

        // mocking
        given(videoMetadataService.getByFilter("", "", "", "test"))
                .willReturn(savedVideoMetadataDTOList);

        // when
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("title", "");
        paramMap.add("uploaderName", "");
        paramMap.add("keyword", "");
        paramMap.add("categoryType", "test");

        final ResultActions actions =
                mockMvc.perform(
                        get(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .params(paramMap)
                                .characterEncoding("UTF-8")
                );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());

    }

}