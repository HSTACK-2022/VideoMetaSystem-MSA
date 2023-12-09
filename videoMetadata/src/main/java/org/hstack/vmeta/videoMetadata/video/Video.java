package org.hstack.vmeta.videoMetadata.video;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name="video")
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String path;

    @Column
    private String uploaderName;

    @Column
    private String thumbnailPath;
}