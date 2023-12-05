package org.hstack.vmeta.videoMetadata.video;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @BeforeEach
    void 초기화() {
        videoRepository.deleteAll();
    }

    @Test
    @Transactional
    void 비디오_저장() {
        // given
        Video video = Video.builder()
                .title("testVideo")
                .path("testPath")
                .uploaderName("testUploader")
                .build();

        // when
        videoRepository.save(video);

        // then
        Video result = videoRepository.findById(video.getId()).get();
        assertThat(video.getId()).isEqualTo(result.getId());
    }

    @Test
    @Transactional
    void 비디오_전체검색() {
        // given
        Video video1 = Video.builder()
                .title("testVideo1")
                .path("testPath1")
                .uploaderName("testUploader1")
                .build();

        Video video2 = Video.builder()
                .title("testVideo2")
                .path("testPath2")
                .uploaderName("testUploader2")
                .build();


        // when
        videoRepository.save(video1);
        videoRepository.save(video2);

        // then
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void 비디오_삭제() {
        // given
        Video video = Video.builder()
                .title("testVideo")
                .path("testPath")
                .uploaderName("testUploader")
                .build();

        // when
        videoRepository.save(video);
        videoRepository.deleteById(video.getId());

        // then
        Optional<Video> result = videoRepository.findById(video.getId());
        assertThat(result).isEmpty();
    }

}