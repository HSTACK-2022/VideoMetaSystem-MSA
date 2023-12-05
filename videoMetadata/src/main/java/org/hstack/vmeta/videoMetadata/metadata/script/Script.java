package org.hstack.vmeta.videoMetadata.metadata.script;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Script {

    private Time time;
    private String content;

}
