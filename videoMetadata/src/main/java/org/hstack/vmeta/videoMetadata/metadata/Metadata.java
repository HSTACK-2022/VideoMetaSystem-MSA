package org.hstack.vmeta.videoMetadata.metadata;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import org.hstack.vmeta.videoMetadata.metadata.category.*;
import org.hstack.vmeta.videoMetadata.metadata.indexScript.IndexScript;
import org.hstack.vmeta.videoMetadata.metadata.keyword.*;
import org.hstack.vmeta.videoMetadata.metadata.narrative.*;
import org.hstack.vmeta.videoMetadata.metadata.presentation.*;
import org.hstack.vmeta.videoMetadata.metadata.script.*;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.*;
import org.hstack.vmeta.videoMetadata.metadata.videoType.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;


@Getter
@Builder
@Entity
@Table(name="metadata")
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Id
    private Long id;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name= "metadata_id")
    private List<Category> category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name= "metadata_id")
    private List<Script> script;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name= "metadata_id")
    private List<Keyword> keyword;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name= "metadata_id")
    private List<IndexScript> indexScript;
}
