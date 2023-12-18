package org.hstack.vmeta.videoMetadata.metadata;

import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.metadata.keyword.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private KeywordRepository keywordRepository;

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
    public void update(MetadataDTO metadataDTO) {
        Long videoId = metadataDTO.getId();
        List<Keyword> newKeyword = metadataDTO.getKeyword();
        keywordRepository.deleteByMid(videoId);
        keywordRepository.saveAll(newKeyword);
    }

    /*
     * MetadataDTO를 id를 기준으로 삭제한다.
     */
    public void delete(Long videoId) {
        metadataRepository.deleteById(videoId);
    }
}
