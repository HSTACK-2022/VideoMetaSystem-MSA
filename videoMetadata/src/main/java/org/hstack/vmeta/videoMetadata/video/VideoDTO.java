package org.hstack.vmeta.videoMetadata.video;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {

    private Long id;

    private String title;

    private String uploaderName;

    private String thumbnailPath;

    public static VideoDTO video2VideoDTO(Video video) {
        return VideoDTO.builder()
                .id(video.getId())
                .title(video.getTitle())
                .uploaderName(video.getUploaderName())
                .thumbnailPath(video.getThumbnailPath())
                .build();
    }

    public Video videoDTO2Video() {
        return Video.builder()
                .title(this.title)
                .uploaderName(this.uploaderName)
                .thumbnailPath(this.thumbnailPath)
                .build();
    }
}
