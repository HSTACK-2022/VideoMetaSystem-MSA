package org.hstack.vmeta.videoMetadata.metadata.index;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Index {

    private Time time;
    private String content;
    private boolean expose;
    private boolean autocreated;
}
