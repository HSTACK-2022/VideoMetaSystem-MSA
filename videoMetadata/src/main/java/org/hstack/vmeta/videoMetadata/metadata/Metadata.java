package org.hstack.vmeta.videoMetadata.metadata;

import jakarta.persistence.*;
import lombok.*;

import org.hstack.vmeta.videoMetadata.metadata.category.*;
import org.hstack.vmeta.videoMetadata.metadata.index.Index;
import org.hstack.vmeta.videoMetadata.metadata.keyword.*;
import org.hstack.vmeta.videoMetadata.metadata.narrative.*;
import org.hstack.vmeta.videoMetadata.metadata.presentation.*;
import org.hstack.vmeta.videoMetadata.metadata.script.*;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.*;
import org.hstack.vmeta.videoMetadata.metadata.videoType.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name="metadata")
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String title;

    @Column
    private String uploaderName;

    @Column
    @Convert(converter = NarrativeAttributeConverter.class)
    private Narrative narrative;

    @Column
    @Convert(converter = PresentationAttributeConverter.class)
    private Presentation presentation;

    @Column
    private Time length;

    @Column
    private Date uploadDate;

    @Column
    private Long videoSize;

    @Column
    @Convert(converter = VideoTypeAttributeConverter.class)
    private VideoType videoType;

    @Column
    @Convert(converter = VideoFrameAttributeConverter.class)
    private VideoFrame videoFrame;

    @Column
    @Convert(converter = CategoryAttributeConverter.class)
    private List<Category> category;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name= "metadata_id", referencedColumnName = "id"))
    private List<Script> script;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name= "metadata_id", referencedColumnName = "id"))
    private List<Keyword> keyword;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name= "metadata_id", referencedColumnName = "id"))
    private List<Index> index;
}
