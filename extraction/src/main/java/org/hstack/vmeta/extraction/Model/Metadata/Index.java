package org.hstack.vmeta.extraction.Model.Metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Index {

    private ContentWithTime contentWithTime;
    private boolean expose;
    private boolean generated;

}
