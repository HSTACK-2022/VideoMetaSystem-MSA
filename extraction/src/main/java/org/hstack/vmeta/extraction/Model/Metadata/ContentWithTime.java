package org.hstack.vmeta.extraction.Model.Metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@Builder
public class ContentWithTime {

    private Time time;
    private String content;

}
