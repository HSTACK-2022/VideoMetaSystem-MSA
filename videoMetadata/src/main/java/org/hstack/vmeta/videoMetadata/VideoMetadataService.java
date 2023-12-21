package org.hstack.vmeta.videoMetadata;

import org.hstack.vmeta.videoMetadata.metadata.MetadataDTO;
import org.hstack.vmeta.videoMetadata.metadata.MetadataService;
import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.category.CategoryType;
import org.hstack.vmeta.videoMetadata.video.VideoDTO;
import org.hstack.vmeta.videoMetadata.video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VideoMetadataService {

    @Autowired
    private VideoService videoService;

    @Autowired
    private MetadataService metadataService;

    /*
     * search용 filtering
     */
    public List<VideoMetadataDTO> getByFilter(String title, String uploaderName, String keyword, String categoryType) {
        // 전처리 - search filter에 따른 idSet 가져오기
        CategoryType categoryTypeEnum = Category.apiVal2CategoryType.get(categoryType);
        List<Long> idList = metadataService.getByFilter(title, uploaderName, keyword, categoryTypeEnum);

        // main - idSet에 따라 video와 metadata 가져오기
        List<VideoDTO> videoDTOList = videoService.getByIdList(idList);
        List<MetadataDTO> metadataDTOList = metadataService.getByIdList(idList);

        // 후처리 - video와 metadata merge
        Map<Long, VideoMetadataDTO> resultMap = new HashMap<>();
        for (VideoDTO v : videoDTOList) {
            Long id = v.getId();
            VideoMetadataDTO vmDTO = VideoMetadataDTO.builder()
                    .id(v.getId())
                    .path(v.getPath())
                    .title(v.getTitle())
                    .uploaderName(v.getUploaderName())
                    .thumbnailPath(v.getThumbnailPath())
                    .build();
            resultMap.put(id, vmDTO);
        }
        for (MetadataDTO m : metadataDTOList) {
            Long id = m.getId();
            VideoMetadataDTO vmDTO = null;
            if (resultMap.containsKey(id)) {
                vmDTO = resultMap.get(id);
                vmDTO.setNarrative(m.getNarrative());
                vmDTO.setPresentation(m.getPresentation());
                vmDTO.setLength(m.getLength());
                vmDTO.setUploadDdate(m.getUploadDate());
                vmDTO.setCategory(m.getCategory());
                vmDTO.setScript(m.getScript());
                vmDTO.setKeyword(m.getKeyword());
                vmDTO.setIndexScript(m.getIndexScript());
            } else {
                vmDTO = VideoMetadataDTO.builder()
                        .id(id)
                        .title(m.getTitle())
                        .uploaderName(m.getUploaderName())
                        .narrative(m.getNarrative())
                        .presentation(m.getPresentation())
                        .length(m.getLength())
                        .uploadDdate(m.getUploadDate())
                        .category(m.getCategory())
                        .script(m.getScript())
                        .keyword(m.getKeyword())
                        .indexScript(m.getIndexScript())
                        .build();
            }
            resultMap.put(id, vmDTO);
        }

        // 반환 - map의 value만 추출
        return resultMap.values().stream().toList();
    }
}
