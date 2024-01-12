package org.hstack.vmeta.extraction.basic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrame;
import org.hstack.vmeta.extraction.basic.videoType.VideoType;

import java.sql.Date;
import java.sql.Time;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicDTO {

    private Time length;

    private Long videoSize;

    private VideoType videoType;

    private VideoFrame videoFrame;

}
