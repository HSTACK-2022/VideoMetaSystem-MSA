package org.hstack.vmeta.videoMetadata.metadata.keyword;

import jakarta.transaction.Transactional;
import org.hstack.vmeta.videoMetadata.metadata.Metadata;
import org.hstack.vmeta.videoMetadata.metadata.MetadataRepository;
import org.hstack.vmeta.videoMetadata.metadata.narrative.Narrative;
import org.hstack.vmeta.videoMetadata.metadata.presentation.Presentation;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.VideoFrame;
import org.hstack.vmeta.videoMetadata.metadata.videoType.VideoType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @BeforeEach
    void 초기화() {
        keywordRepository.deleteAll();
        metadataRepository.deleteAll();
        metadataRepository.save(Metadata.builder()
                .id(1L)
                .narrative(Narrative.APPLICATION)
                .presentation(Presentation.DYNAMIC)
                .length(new Time(0, 30, 10))
                .uploadDate(new Date(2023, 12, 5))
                .videoSize(0L)
                .videoType(VideoType.MP4)
                .videoFrame(VideoFrame.FPS24)
                .build());
        metadataRepository.save(Metadata.builder()
                .id(2L)
                .narrative(Narrative.APPLICATION)
                .presentation(Presentation.DYNAMIC)
                .length(new Time(0, 30, 10))
                .uploadDate(new Date(2023, 12, 5))
                .videoSize(0L)
                .videoType(VideoType.MP4)
                .videoFrame(VideoFrame.FPS24)
                .build());
    }

    @Test
    @Transactional
    @DisplayName("Keyword List 저장")
    void saveAll() {
        // given
        List<Keyword> keywordList = new ArrayList<>();
        keywordList.add(Keyword.builder()
                .keyword("testKeyword")
                .perc(0.5F)
                .expose(true)
                .autocreated(true)
                .mid(1L)
                .build());
        keywordList.add(Keyword.builder()
                .keyword("testKeyword2")
                .perc(0.3F)
                .expose(true)
                .autocreated(true)
                .mid(1L)
                .build());

        // when
        keywordRepository.saveAll(keywordList);

        // then
        assertThat(keywordRepository.findAll().size()).isEqualTo(keywordList.size());
    }

    @Test
    @Transactional
    @DisplayName("mid로 Keyword 삭제")
    void deleteByMid() {
        // given
        List<Keyword> keywordList = new ArrayList<>();
        keywordList.add(Keyword.builder()
                .keyword("testKeyword")
                .perc(0.5F)
                .expose(true)
                .autocreated(true)
                .mid(1L)
                .build());
        keywordList.add(Keyword.builder()
                .keyword("testKeyword2")
                .perc(0.3F)
                .expose(true)
                .autocreated(true)
                .mid(1L)
                .build());
        keywordList.add(Keyword.builder()
                .keyword("testKeyword3")
                .perc(0.2F)
                .expose(true)
                .autocreated(true)
                .mid(2L)
                .build());

        // when
        keywordRepository.saveAll(keywordList);
        keywordRepository.deleteByMid(1L);

        // then
        assertThat(keywordRepository.findAll().size()).isEqualTo(1);
    }
}