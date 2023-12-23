package org.hstack.vmeta.videoMetadata;

import org.hstack.vmeta.videoMetadata.metadata.MetadataDTO;
import org.hstack.vmeta.videoMetadata.metadata.MetadataService;
import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.video.VideoDTO;
import org.hstack.vmeta.videoMetadata.video.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VideoMetadataServiceTest {

    @Mock
    private VideoService videoService;

    @Mock
    private MetadataService metadataService;

    @InjectMocks
    private VideoMetadataService videoMetadataService;

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 제목")
    void getByFilterTitle() {
        // given
        String findText = "test";

        List<VideoDTO> videoList = new ArrayList<>();
        videoList.add(VideoDTO.builder().id(1L).title(findText).build());

        List<MetadataDTO> metadataList = new ArrayList<>();
        metadataList.add(MetadataDTO.builder().id(1L).title(findText).build());

        List<Long> savedIdList = new ArrayList<>();
        savedIdList.add(1L);

        // mocking
        given(metadataService.getByFilter(findText, null, null, null)).willReturn(savedIdList);
        given(videoService.getByIdList(savedIdList)).willReturn(videoList);
        given(metadataService.getByIdList(savedIdList)).willReturn(metadataList);

        // when
        List<VideoMetadataDTO> resList = videoMetadataService.getByFilter(
                findText,
                null,
                null,
                null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 업로더")
    void getByFilterUploaderName() {
        // given
        String findText = "test";

        List<VideoDTO> videoList = new ArrayList<>();
        videoList.add(VideoDTO.builder().id(1L).uploaderName(findText).build());

        List<MetadataDTO> metadataList = new ArrayList<>();
        metadataList.add(MetadataDTO.builder().id(1L).uploaderName(findText).build());

        List<Long> savedIdList = new ArrayList<>();
        savedIdList.add(1L);

        // mocking
        given(metadataService.getByFilter(null, findText, null, null)).willReturn(savedIdList);
        given(videoService.getByIdList(savedIdList)).willReturn(videoList);
        given(metadataService.getByIdList(savedIdList)).willReturn(metadataList);

        // when
        List<VideoMetadataDTO> resList = videoMetadataService.getByFilter(
                null,
                findText,
                null,
                null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 키워드")
    void getByFilterKeyword() {
        // given
        String findText = "test";
        List<Keyword> keywordList = new ArrayList<>();
        keywordList.add(Keyword.builder().keyword(findText).build());

        List<MetadataDTO> metadataList = new ArrayList<>();
        metadataList.add(MetadataDTO.builder().id(1L).keyword(keywordList).build());

        List<Long> savedIdList = new ArrayList<>();
        savedIdList.add(1L);

        // mocking
        given(metadataService.getByFilter(null, null, findText, null)).willReturn(savedIdList);
        given(metadataService.getByIdList(savedIdList)).willReturn(metadataList);

        // when
        List<VideoMetadataDTO> resList = videoMetadataService.getByFilter(
                null,
                null,
                findText,
                null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 카테고리")
    void getByFilterCategory() {
        // given
        String findText = "TMI_HW";
        CategoryType categoryType = Category.apiVal2CategoryType.get(findText);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.builder().categoryType(categoryType).build());

        List<MetadataDTO> metadataList = new ArrayList<>();
        metadataList.add(MetadataDTO.builder().id(1L).category(categoryList).build());

        List<Long> savedIdList = new ArrayList<>();
        savedIdList.add(1L);

        // mocking
        given(metadataService.getByFilter(null, null, null, categoryType)).willReturn(savedIdList);
        given(metadataService.getByIdList(savedIdList)).willReturn(metadataList);

        // when
        List<VideoMetadataDTO> resList = videoMetadataService.getByFilter(
                null,
                null,
                null,
                findText);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }
}