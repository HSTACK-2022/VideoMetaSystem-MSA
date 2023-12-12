package org.hstack.vmeta.videoMetadata.metadata;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.indexScript.IndexScript;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.metadata.narrative.Narrative;
import org.hstack.vmeta.videoMetadata.metadata.narrative.NarrativeAttributeConverter;
import org.hstack.vmeta.videoMetadata.metadata.presentation.Presentation;
import org.hstack.vmeta.videoMetadata.metadata.presentation.PresentationAttributeConverter;
import org.hstack.vmeta.videoMetadata.metadata.script.Script;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.VideoFrame;
import org.hstack.vmeta.videoMetadata.metadata.videoFrame.VideoFrameAttributeConverter;
import org.hstack.vmeta.videoMetadata.metadata.videoType.VideoType;
import org.hstack.vmeta.videoMetadata.metadata.videoType.VideoTypeAttributeConverter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/*
 * extraction과 소통할 순수 Metadata
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDTO {

    private Long id;

    private String title;

    private String uploaderName;

    private Narrative narrative;

    private Presentation presentation;

    private Time length;

    private Date uploadDate;

    private Long videoSize;

    private VideoType videoType;

    private VideoFrame videoFrame;

    private List<Category> category;

    private List<Script> script;

    private List<Keyword> keyword;

    private List<IndexScript> indexScript;
}
