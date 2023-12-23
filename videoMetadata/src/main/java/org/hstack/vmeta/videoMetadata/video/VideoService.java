package org.hstack.vmeta.videoMetadata.video;

import org.hstack.vmeta.videoMetadata.metadata.MetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    /*
     * VideoRepository에서 모든 Video를 받아
     * VideoDTO로 변환 후 반환한다.
     */
    public List<VideoDTO> getAll() {
        return videoRepository.findAll()
                .stream()
                .map(v -> VideoDTO.toVideoDTO(v))
                .collect(Collectors.toList());
    }

    /*
     * 특정 id list에 해당하는 Video를 가져온다.
     * videoMetadata에서 써먹기 위함
     */
    public List<VideoDTO> getByIdList(List<Long> idList) {
        return videoRepository.findAllById(idList)
                .stream()
                .map(v -> VideoDTO.toVideoDTO(v))
                .collect(Collectors.toList());
    }

    /*
     * videoDTO를 video로 변환 후 저장한다.
     */
    public Long save(VideoDTO videoDTO) {
        return videoRepository.save(videoDTO.toVideo()).getId();
    }

    /*
     * videoDTO의 id를 기준으로 삭제한다.
     */
    public void delete(Long videoId) {
        videoRepository.deleteById(videoId);
    }
}
