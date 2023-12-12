package org.hstack.vmeta.videoMetadata.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hstack.vmeta.videoMetadata.metadata.category.Category;
import org.hstack.vmeta.videoMetadata.metadata.indexScript.IndexScript;
import org.hstack.vmeta.videoMetadata.metadata.keyword.Keyword;
import org.hstack.vmeta.videoMetadata.metadata.narrative.Narrative;
import org.hstack.vmeta.videoMetadata.metadata.presentation.Presentation;
import org.hstack.vmeta.videoMetadata.metadata.script.Script;

import java.sql.Date;
import java.sql.Time;
import java.util.List;


/*
 * search, detailView 등 user에게 보여지는 영역용
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadataDTO {

    private Long Id;

    private String title;

    private String path;

    private String uploaderName;

    private String thumbnailPath;

    private Narrative narrative;

    private Presentation presentation;

    private Time length;

    private Date uploadDdate;

    private List<Category> category;

    private List<Script> script;

    private List<Keyword> keyword;

    private List<IndexScript> indexScript;
}
