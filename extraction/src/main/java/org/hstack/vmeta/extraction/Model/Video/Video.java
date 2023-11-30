package org.hstack.vmeta.extraction.Model.Video;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
public class Video {

    private Long Id;
    private String title;
    private String path;

}
