package org.hstack.vmeta.videoMetadata.metadata;

import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.metadata.keyword.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    /*
     * 특정 id list에 해당하는 Metadata를 가져온다.
     * videoMetadata에서 써먹기 위함
     */
    public List<MetadataDTO> getByIdList(List<Long> idList) {
        return metadataRepository.findAllById(idList)
                .stream()
                .map(m -> MetadataDTO.toMetadataDTO(m))
                .collect(Collectors.toList());
    }

    /*
     * search용 filtering
     * title, uploaderName, keyword, category가 포함된 idSet를 반환한다.
     */
    public List<Long> getByFilter(String title, String uploaderName, String keyword, CategoryType categoryType) {
        List<MetadataMapping> idList = new ArrayList<>();
        idList.addAll(metadataRepository.findByTitleContains(title));
        idList.addAll(metadataRepository.findByUploaderNameContains(uploaderName));
        idList.addAll(metadataRepository.findByKeywordContains(keyword));
        idList.addAll(metadataRepository.findByCategory(categoryType));

        Set<Long> idSet = new HashSet<>();
        for (MetadataMapping mm : idList) {
            idSet.add(mm.getId());
        }

        return idSet.stream().toList();
    }

    /*
     * MetadataDTO를 Metadata로 변환 후 저장한다.
     */
    public Long save(MetadataDTO metadataDTO) {
        return metadataRepository.save(metadataDTO.toMetadata()).getId();
    }

    /*
     * 기존 MetadataDTO를 새로운 MetadataDTO로 교체한다.
     * 아직은 Keyword만 교체 가능하기에 이에 한정해서 코드 작성 ('23.12.18)
     * - 추후 업데이트 필요
     * - front단에서 어떻게 keyword를 받아올것인가에 대한 고민 필요
     */
    public Long update(MetadataDTO metadataDTO) {
        Long videoId = metadataDTO.getId();
        List<Keyword> newKeyword = metadataDTO.getKeyword();
        keywordRepository.deleteByMid(videoId);
        keywordRepository.saveAll(newKeyword);
        return videoId;
    }

    /*
     * MetadataDTO를 id를 기준으로 삭제한다.
     */
    public void delete(Long videoId) {
        metadataRepository.deleteById(videoId);
    }
}
