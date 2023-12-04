package org.hstack.vmeta.videoMetadata.metadata.index;

import jakarta.persistence.Embeddable;

import java.sql.Time;

@Embeddable
public class Index {

    private Time time;
    private String content;
    private boolean expose;
    private boolean autocreated;
}
