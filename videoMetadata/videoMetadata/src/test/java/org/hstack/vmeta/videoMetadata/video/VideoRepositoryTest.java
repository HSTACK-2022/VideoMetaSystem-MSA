package org.hstack.vmeta.videoMetadata.video;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @AfterEach
    void 초기화() {
        videoRepository.deleteAll();
    }

    @Test
    @Transactional
    void 비디오저장() {
        // given
        Video video = Video.builder()
                .title("testVideo")
                .path("testPath")
                .build();

        // when
        videoRepository.save(video);

        // then
        Video result = videoRepository.findById(video.getId()).get();
        assertThat(video.getId()).isEqualTo(result.getId());
    }

    @Test
    @Transactional
    void 비디오전체선택() {
        // given
        Video video1 = Video.builder()
                .title("testVideo1")
                .path("testPath1")
                .build();

        Video video2 = Video.builder()
                .title("testVideo2")
                .path("testPath2")
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
    void 비디오삭제() {
        // given
        Video video = Video.builder()
                .title("testVideo")
                .path("testPath")
                .build();

        // when
        videoRepository.save(video);
        videoRepository.delete(video);

        // then
        Optional<Video> result = videoRepository.findById(video.getId());
        assertThat(result).isEmpty();
    }

}