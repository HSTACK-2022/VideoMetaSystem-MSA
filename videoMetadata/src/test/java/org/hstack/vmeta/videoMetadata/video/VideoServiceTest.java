package org.hstack.vmeta.videoMetadata.video;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;


    @BeforeEach
    void beforeEach() {
        videoRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 비디오 조회")
    void getAll() {
        // given
        List<Video> videoList = new ArrayList<>();
        videoList.add(Video.builder().title("testTitle1").build());
        videoList.add(Video.builder().title("testTitle2").build());
        videoList.add(Video.builder().title("testTitle3").build());

        List<Video> savedList = videoList;

        // mocking
        given(videoRepository.findAll()).willReturn(savedList);

        // when
        List<VideoDTO> resList = videoService.getAll();

        // then
        assertThat(resList.size()).isEqualTo(videoList.size());
        for (int i = 0; i < resList.size(); i++) {
            Video video = videoList.get(i);
            VideoDTO resVideoDTO = resList.get(i);
            assertThat(resVideoDTO.getTitle()).isEqualTo(video.getTitle());
        }
    }

    @Test
    @DisplayName("비디오 저장")
    @Transactional
    void save() {
        // given
        VideoDTO videoDTO = VideoDTO.builder().title("testTitle").build();
        Video saveVideo = videoDTO.videoDTO2Video();

        // mocking
        given(videoRepository.save(any())).willReturn(videoDTO.videoDTO2Video());

        // when
        Long newId = videoService.save(videoDTO);

        // then
        assertThat(newId).isEqualTo(saveVideo.getId());
    }

    @Test
    @DisplayName("비디오 삭제")
    @Transactional
    void delete() {
        // given
        Long videoId = 1L;

        // mocking

        // when
        videoService.delete(videoId);

        // then
        verify(videoRepository).deleteById(videoId);
    }
}