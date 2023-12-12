package org.hstack.vmeta.videoMetadata.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /*
     * VideoRepository에서 모든 Video를 받아
     * VideoDTO로 변환 후 반환한다.
     */
    public List<VideoDTO> getAll() {
        return videoRepository.findAll()
                .stream()
                .map(v -> VideoDTO.video2VideoDTO(v))
                .collect(Collectors.toList());
    }

    /*
     * videoDTO를 video로 변환 후 저장한다.
     */
    public Long save(VideoDTO videoDTO) {
        return videoRepository.save(videoDTO.videoDTO2Video());
    }

    /*
     * videoDTO의 id를 기준으로 삭제한다.
     */
    public void delete(VideoDTO videoDTO) {
        videoRepository.deleteById(videoDTO.getId());
    }
}
