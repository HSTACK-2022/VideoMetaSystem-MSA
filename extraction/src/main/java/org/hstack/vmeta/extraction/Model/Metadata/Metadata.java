package org.hstack.vmeta.extraction.Model.Metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Builder
@Getter
@Setter
public class Metadata {

    private Long Id;
    private String title;
    private String uploader;
    private Narrative narrative;
    private Presentation presentation;
    private Time length;
    private Date uploadDate;
    private long videoSize;
    private VideoType videoType;

    private List<ContentWithTime> script;
    private List<Category> category;
    private List<Keyword> keyword;
    private List<Index> index;
}
