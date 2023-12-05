package org.hstack.vmeta.videoMetadata.metadata;

import jakarta.transaction.Transactional;
import org.hstack.vmeta.videoMetadata.metadata.category.*;
import org.hstack.vmeta.videoMetadata.metadata.script.*;
import org.hstack.vmeta.videoMetadata.metadata.keyword.*;
import org.hstack.vmeta.videoMetadata.metadata.index.*;
import org.hstack.vmeta.videoMetadata.metadata.narrative.*;
import org.hstack.vmeta.videoMetadata.metadata.presentation.*;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.*;
import org.hstack.vmeta.videoMetadata.metadata.videoType.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MetadataRepositoryTest {

    @Autowired
    private MetadataRepository metadataRepository;

    @BeforeEach
    void 초기화() {
        metadataRepository.deleteAll();
    }

    @Test
    @Transactional
    void 메타데이터_저장() {
        // given
        Metadata metadata = Metadata.builder()
                .id(1L)
                .title("test title")
                .uploaderName("test uploader")
                .narrative(Narrative.APPLICATION)
                .presentation(Presentation.DYNAMIC)
                .length(new Time(0, 30, 10))
                .uploadDate(new Date(2023, 12, 5))
                .videoSize(0L)
                .videoType(VideoType.MP4)
                .videoFrame(VideoFrame.FPS24)
                .category(List.of(
                        Category.builder()
                        .categoryType(CategoryType.IT)
                        .perc(1.0F)
                        .build()))
                .script(List.of(
                        Script.builder()
                                .time(new Time(0, 0, 0))
                                .content("test script")
                                .build()))
                .keyword(List.of(
                        Keyword.builder()
                                .keyword("test keyword")
                                .perc(1.0F)
                                .expose(true)
                                .autocreated(true)
                                .build()))
                .index(List.of(
                        Index.builder()
                                .time(new Time(0, 0, 0))
                                .content("test index")
                                .expose(true)
                                .autocreated(true)
                                .build()))
                .build();

        // when
        metadataRepository.save(metadata);

        // then
        Metadata resMD = metadataRepository.findById(metadata.getId()).get();
        assertThat(resMD.getId()).isEqualTo(metadata.getId());
    }

    @Test
    @Transactional
    void 메타데이터_전체검색() {
        // given
        for (long id = 1; id <= 5; id++) {
            Metadata metadata = Metadata.builder()
                    .id(id)
                    .title("test title")
                    .uploaderName("test uploader")
                    .narrative(Narrative.APPLICATION)
                    .presentation(Presentation.DYNAMIC)
                    .length(new Time(0, 30, 10))
                    .uploadDate(new Date(2023, 12, 5))
                    .videoSize(0L)
                    .videoType(VideoType.MP4)
                    .videoFrame(VideoFrame.FPS24)
                    .category(List.of(
                            Category.builder()
                                    .categoryType(CategoryType.IT)
                                    .perc(1.0F)
                                    .build()))
                    .script(List.of(
                            Script.builder()
                                    .time(new Time(0, 0, 0))
                                    .content("test script")
                                    .build()))
                    .keyword(List.of(
                            Keyword.builder()
                                    .keyword("test keyword")
                                    .perc(1.0F)
                                    .expose(true)
                                    .autocreated(true)
                                    .build()))
                    .index(List.of(
                            Index.builder()
                                    .time(new Time(0, 0, 0))
                                    .content("test index")
                                    .expose(true)
                                    .autocreated(true)
                                    .build()))
                    .build();

            metadataRepository.save(metadata);
        }

        // when
        List<Metadata> resMDList = metadataRepository.findAll();

        // then
        assertThat(resMDList.size()).isEqualTo(5);
    }


    /*

    @Test
    @Transactional
    void 메타데이터_제목검색() {
        // given
        Metadata metadata1 = Metadata.builder()
                .id(1L)
                .title("1test title")
                .build();

        Metadata metadata2 = Metadata.builder()
                .id(2L)
                .title("test title2")
                .build();

        Metadata metadata3 = Metadata.builder()
                .id(3L)
                .title("none")
                .build();

        metadataRepository.save(metadata1);
        metadataRepository.save(metadata2);
        metadataRepository.save(metadata3);

        // when
        List<Metadata> resMDList = metadataRepository.findByTitleContains("test");

        // then
        assertThat(resMDList.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void 메타데이터_키워드검색() {
        // given
        Metadata metadata1 = Metadata.builder()
                .id(1L)
                .keyword(List.of(
                        Keyword.builder().keyword("test1").perc(0.5F).expose(true).autocreated(true).build(),
                        Keyword.builder().keyword("aaaa").perc(0.5F).expose(true).autocreated(true).build()))
                .build();

        Metadata metadata2 = Metadata.builder()
                .id(2L)
                .keyword(List.of(
                        Keyword.builder().keyword("2test").perc(0.5F).expose(true).autocreated(true).build(),
                        Keyword.builder().keyword("bbbb").perc(0.5F).expose(true).autocreated(true).build()))
                .build();

        Metadata metadata3 = Metadata.builder()
                .id(3L)
                .keyword(List.of(
                        Keyword.builder().keyword("none").perc(0.5F).expose(true).autocreated(true).build(),
                        Keyword.builder().keyword("cccc").perc(0.5F).expose(true).autocreated(true).build()))
                .build();

        metadataRepository.save(metadata1);
        metadataRepository.save(metadata2);
        metadataRepository.save(metadata3);

        // when
        List<Metadata> resMDList = metadataRepository.findByKeywordContains("test");

        // then
        assertThat(resMDList.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void 메타데이터_카테고리검색() {
        // given
        Metadata metadata1 = Metadata.builder()
                .id(1L)
                .category(List.of(
                        Category.builder().categoryType(CategoryType.IT).perc(0.5F).build(),
                        Category.builder().categoryType(CategoryType.ART).perc(0.5F).build()))
                .build();

        Metadata metadata2 = Metadata.builder()
                .id(2L)
                .category(List.of(
                        Category.builder().categoryType(CategoryType.IT).perc(0.5F).build(),
                        Category.builder().categoryType(CategoryType.LAW).perc(0.5F).build()))
                .build();

        Metadata metadata3 = Metadata.builder()
                .id(3L)
                .category(List.of(
                        Category.builder().categoryType(CategoryType.FOOD).perc(0.5F).build(),
                        Category.builder().categoryType(CategoryType.MEDIA).perc(0.5F).build()))
                .build();

        metadataRepository.save(metadata1);
        metadataRepository.save(metadata2);
        metadataRepository.save(metadata3);

        // when
        List<Metadata> resMDList = metadataRepository.findByCategoryContains("test");

        // then
        assertThat(resMDList.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void 메타데이터_업로더검색() {
        // given
        Metadata metadata1 = Metadata.builder()
                .id(1L)
                .uploaderName("test")
                .build();

        Metadata metadata2 = Metadata.builder()
                .id(2L)
                .uploaderName("2test2")
                .build();

        Metadata metadata3 = Metadata.builder()
                .id(3L)
                .uploaderName("none")
                .build();

        metadataRepository.save(metadata1);
        metadataRepository.save(metadata2);
        metadataRepository.save(metadata3);

        // when
        List<Metadata> resMDList = metadataRepository.findByUploaderNameContains("test");

        // then
        assertThat(resMDList.size()).isEqualTo(2);
    }

     */

    @Test
    @Transactional
    void 메타데이터_삭제() {
        // given
        Metadata metadata = Metadata.builder()
                .id(1L)
                .title("test title")
                .uploaderName("test uploader")
                .narrative(Narrative.APPLICATION)
                .presentation(Presentation.DYNAMIC)
                .length(new Time(0, 30, 10))
                .uploadDate(new Date(2023, 12, 5))
                .videoSize(0L)
                .videoType(VideoType.MP4)
                .videoFrame(VideoFrame.FPS24)
                .category(List.of(
                        Category.builder()
                                .categoryType(CategoryType.IT)
                                .perc(1.0F)
                                .build()))
                .script(List.of(
                        Script.builder()
                                .time(new Time(0, 0, 0))
                                .content("test script")
                                .build()))
                .keyword(List.of(
                        Keyword.builder()
                                .keyword("test keyword")
                                .perc(1.0F)
                                .expose(true)
                                .autocreated(true)
                                .build()))
                .index(List.of(
                        Index.builder()
                                .time(new Time(0, 0, 0))
                                .content("test index")
                                .expose(true)
                                .autocreated(true)
                                .build()))
                .build();

        // when
        metadataRepository.save(metadata);
        metadataRepository.deleteById(metadata.getId());

        // then
        assertThat(metadataRepository.findById(metadata.getId())).isEmpty();
    }
}