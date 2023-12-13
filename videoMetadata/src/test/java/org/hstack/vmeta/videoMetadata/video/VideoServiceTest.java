package org.hstack.vmeta.videoMetadata.video;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;


    @BeforeEach
    void beforeEach() {
        videoRepository.deleteAll();
    }

    @Test
    void getAll() {
        // given
        VideoDTO videoDTO1 = VideoDTO.builder()
                .title("testTitle1")
                .uploaderName("testUploader1")
                .build();

        VideoDTO videoDTO2 = VideoDTO.builder()
                .title("testTitle2")
                .uploaderName("testUploader2")
                .build();

        videoService.save(videoDTO1);
        videoService.save(videoDTO2);

        // when
        List<VideoDTO> resList = videoService.getAll();

        // then
        assertThat(resList.size()).isEqualTo(2);
    }

    @Test
    void save() {
        // given
        VideoDTO videoDTO = VideoDTO.builder()
                .title("testTitle")
                .uploaderName("testUploader")
                .build();

        // when
        Long newId = videoService.save(videoDTO);

        // then
        assertThat(newId).isNotNull();
    }

    @Test
    void delete() {
        // given
        VideoDTO videoDTO = VideoDTO.builder()
                .id(1L)
                .title("testTitle")
                .uploaderName("testUploader")
                .build();

        videoService.save(videoDTO);

        // when
        videoService.delete(videoDTO);

        // then
        assertThat(videoRepository.findById(videoDTO.getId())).isEmpty();
    }
}