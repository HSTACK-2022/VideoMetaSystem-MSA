package org.hstack.vmeta.extraction;

import lombok.Builder;
import org.hstack.vmeta.extraction.audio.AudioDTO;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrame;
import org.hstack.vmeta.extraction.basic.videoType.VideoType;
import org.hstack.vmeta.extraction.category.CategoryDTO;
import org.hstack.vmeta.extraction.indexScript.IndexScriptDTO;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.hstack.vmeta.extraction.scene.narrative.Narrative;
import org.hstack.vmeta.extraction.scene.presentation.Presentation;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Builder
public class MetadataDTO {

    private Long id;

    private Narrative narrative;

    private Presentation presentation;

    private Time length;

    private Date uploadDate;

    private Long videoSize;

    private VideoType videoType;

    private VideoFrame videoFrame;

    private List<CategoryDTO.Category> category;

    private List<AudioDTO.Script> script;

    private List<KeywordDTO.Keyword> keyword;

    private List<IndexScriptDTO.IndexScript> indexScript;
}
