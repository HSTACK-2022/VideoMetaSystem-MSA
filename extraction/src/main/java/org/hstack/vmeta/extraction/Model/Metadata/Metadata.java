package org.hstack.vmeta.extraction.Model.Metadata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
public class Metadata {

    private Long Id;

    private String title;


    private String uploader;
    private Narrative narrative;
    private Presentation presentation;
    private Time length;
    private Date uploadDate;
    private Long videoSize;
    private VideoType videoType;
    private VideoFrame videoFrame;

    private List<ContentWithTime> script;
    private List<Category> category;
    private List<Keyword> keyword;
    private List<Index> index;
}
