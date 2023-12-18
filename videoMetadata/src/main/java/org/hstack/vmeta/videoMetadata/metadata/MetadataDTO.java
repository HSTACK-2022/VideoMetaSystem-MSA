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

    public static MetadataDTO toMetadataDTO(Metadata metadata) {
        return MetadataDTO.builder()
                .id(metadata.getId())
                .title(metadata.getTitle())
                .uploaderName(metadata.getUploaderName())
                .narrative(metadata.getNarrative())
                .presentation(metadata.getPresentation())
                .length(metadata.getLength())
                .uploadDate(metadata.getUploadDate())
                .videoSize(metadata.getVideoSize())
                .videoType(metadata.getVideoType())
                .videoFrame(metadata.getVideoFrame())
                .category(metadata.getCategory())
                .script(metadata.getScript())
                .keyword(metadata.getKeyword())
                .indexScript(metadata.getIndexScript())
                .build();
    }

    public Metadata toMetadata() {
        return Metadata.builder()
                .id(this.id)
                .title(this.title)
                .uploaderName(this.uploaderName)
                .narrative(this.narrative)
                .presentation(this.presentation)
                .length(this.length)
                .uploadDate(this.uploadDate)
                .videoSize(this.videoSize)
                .videoType(this.videoType)
                .videoFrame(this.videoFrame)
                .category(this.category)
                .script(this.script)
                .keyword(this.keyword)
                .indexScript(this.indexScript)
                .build();
    }
}
