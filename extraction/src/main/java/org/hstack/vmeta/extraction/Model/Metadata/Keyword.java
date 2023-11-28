package org.hstack.vmeta.extraction.Model.Metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Keyword {

    private String keyword;
    private float perc;
    private boolean expose;
    private boolean generated;

}
