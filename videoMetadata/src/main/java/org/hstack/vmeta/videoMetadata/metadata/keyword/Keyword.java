package org.hstack.vmeta.videoMetadata.metadata.keyword;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {
    private String keyword;
    private float perc;
    private boolean expose;
    private boolean autocreated;
}
