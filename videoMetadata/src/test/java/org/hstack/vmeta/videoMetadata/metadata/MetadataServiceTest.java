package org.hstack.vmeta.videoMetadata.metadata;

import jakarta.transaction.Transactional;
import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.metadata.keyword.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

    @Mock
    private MetadataRepository metadataRepository;

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private MetadataService metadataService;

    @BeforeEach
    void beforeEach() {
        keywordRepository.deleteAll();
        metadataRepository.deleteAll();
    }

    @Test
    @DisplayName("특정 메타데이터 조회")
    void getByIdList() {
        // given
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);

        List<Metadata> videoList = new ArrayList<>();
        videoList.add(Metadata.builder().id(1L).build());
        videoList.add(Metadata.builder().id(2L).build());
        videoList.add(Metadata.builder().id(3L).build());

        List<Metadata> savedList = videoList.subList(0, idList.size());

        // mocking
        given(metadataRepository.findAllById(idList)).willReturn(savedList);

        // when
        List<MetadataDTO> resList = metadataService.getByIdList(idList);

        // then
        assertThat(resList.size()).isEqualTo(idList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 제목")
    void getByFilterTitle() {
        // given
        String findText = "test";

        List<Metadata> videoList = new ArrayList<>();
        videoList.add(Metadata.builder().id(1L).title(findText).build());
        videoList.add(Metadata.builder().id(2L).title(findText).build());
        videoList.add(Metadata.builder().id(3L).title("user").build());

        Set<MetadataMapping> savedIdList = new HashSet<>();
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 1L;
            }
        });
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 2L;
            }
        });

        // mocking
        given(metadataRepository.findByTitleContains(findText)).willReturn(savedIdList);

        // when
        List<Long> resList = metadataService.getByFilter(findText, null, null, null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 업로더")
    void getByFilterUploaderName() {
        // given
        String findText = "test";

        List<Metadata> videoList = new ArrayList<>();
        videoList.add(Metadata.builder().id(1L).uploaderName(findText).build());
        videoList.add(Metadata.builder().id(2L).uploaderName(findText).build());
        videoList.add(Metadata.builder().id(3L).uploaderName("user").build());

        Set<MetadataMapping> savedIdList = new HashSet<>();
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 1L;
            }
        });
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 2L;
            }
        });

        // mocking
        given(metadataRepository.findByUploaderNameContains(findText)).willReturn(savedIdList);

        // when
        List<Long> resList = metadataService.getByFilter(null, findText, null, null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 키워드")
    void getByFilterKeyword() {
        // given
        String findText = "test";
        List<Keyword> keywordList1 = new ArrayList<>();
        keywordList1.add(Keyword.builder().keyword(findText).build());
        List<Keyword> keywordList2 = new ArrayList<>();
        keywordList2.add(Keyword.builder().keyword("user").build());

        List<Metadata> videoList = new ArrayList<>();
        videoList.add(Metadata.builder().id(1L).keyword(keywordList1).build());
        videoList.add(Metadata.builder().id(2L).keyword(keywordList1).build());
        videoList.add(Metadata.builder().id(3L).keyword(keywordList2).build());

        Set<MetadataMapping> savedIdList = new HashSet<>();
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 1L;
            }
        });
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 2L;
            }
        });

        // mocking
        given(metadataRepository.findByKeywordContains(findText)).willReturn(savedIdList);

        // when
        List<Long> resList = metadataService.getByFilter(null, null, findText, null);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }


    @Test
    @DisplayName("검색 조건으로 메타데이터 조회 - 카테고리")
    void getByFilterCategoryType() {
        // given
        CategoryType findType = CategoryType.IT;
        List<Category> categoryList1 = new ArrayList<>();
        categoryList1.add(Category.builder().categoryType(findType).build());
        List<Category> categoryList2 = new ArrayList<>();
        categoryList2.add(Category.builder().categoryType(CategoryType.ART).build());

        List<Metadata> videoList = new ArrayList<>();
        videoList.add(Metadata.builder().id(1L).category(categoryList1).build());
        videoList.add(Metadata.builder().id(2L).category(categoryList1).build());
        videoList.add(Metadata.builder().id(3L).category(categoryList2).build());

        Set<MetadataMapping> savedIdList = new HashSet<>();
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 1L;
            }
        });
        savedIdList.add(new MetadataMapping() {
            @Override
            public Long getId() {
                return 2L;
            }
        });

        // mocking
        given(metadataRepository.findByCategory(findType)).willReturn(savedIdList);

        // when
        List<Long> resList = metadataService.getByFilter(null, null, null, findType);

        // then
        assertThat(resList.size()).isEqualTo(savedIdList.size());
    }

    @Test
    @DisplayName("메타데이터 저장")
    @Transactional
    void save() {
        // given
        MetadataDTO metadataDTO = MetadataDTO.builder().build();
        Metadata saveMetadata = metadataDTO.toMetadata();

        // mocking
        given(metadataRepository.save(any())).willReturn(metadataDTO.toMetadata());

        // when
        Long newId = metadataService.save(metadataDTO);

        // then
        assertThat(newId).isEqualTo(saveMetadata.getId());
    }

    @Test
    @DisplayName("메타데이터 수정 - 키워드")
    @Transactional
    void update() {
        // given
        Long mid = 0L;

        List<Long> idList = new ArrayList<>();
        idList.add(mid);

        List<Keyword> newKeywordList = new ArrayList<>();
        newKeywordList.add(Keyword.builder().id(10L).mid(mid).build());
        newKeywordList.add(Keyword.builder().id(20L).mid(mid).build());

        MetadataDTO newMetadataDTO = MetadataDTO.builder()
                .id(mid)
                .keyword(newKeywordList)
                .build();
        
        List<Metadata> savedMetadataList = new ArrayList<>();
        savedMetadataList.add(newMetadataDTO.toMetadata());


        // mocking
        given(keywordRepository.saveAll(newKeywordList)).willReturn(newKeywordList);
        given(metadataRepository.findAllById(idList)).willReturn(savedMetadataList);

        // when
        metadataService.update(newMetadataDTO);

        List<Keyword> keywordList = metadataService.getByIdList(idList)
                .get(0)
                .getKeyword();

        // then
        assertThat(keywordList.size()).isEqualTo(newMetadataDTO.getKeyword().size());
        for (int i = 0; i < keywordList.size(); i++) {
            assertThat(keywordList.get(i).getId()).isEqualTo(newKeywordList.get(i).getId());
        }
    }

    @Test
    @DisplayName("메타데이터 삭제")
    @Transactional
    void delete() {
        // given
        Long videoId = 1L;

        // mocking

        // when
        metadataService.delete(videoId);

        // then
        verify(metadataRepository).deleteById(videoId);
    }
}