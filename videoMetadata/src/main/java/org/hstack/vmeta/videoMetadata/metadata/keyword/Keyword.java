package org.hstack.vmeta.videoMetadata.metadata.keyword;

import jakarta.persistence.Embeddable;

@Embeddable
public class Keyword {
    private String keyword;
    private float perc;
    private boolean expose;
    private boolean autocreated;
}
