package org.hstack.vmeta.videoMetadata.metadata.script;

import jakarta.persistence.Embeddable;

import java.sql.Time;

@Embeddable
public class Script {

    private Time time;
    private String content;

}
