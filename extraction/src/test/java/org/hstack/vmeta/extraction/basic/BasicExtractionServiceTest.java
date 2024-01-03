package org.hstack.vmeta.extraction.basic;

import org.apache.commons.io.FilenameUtils;
import org.hstack.vmeta.extraction.basic.time.TimeConverter;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrame;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrameAttributeConverter;
import org.hstack.vmeta.extraction.basic.videoType.VideoType;
import org.hstack.vmeta.extraction.basic.videoType.VideoTypeAttributeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest
class BasicExtractionServiceTest {

    String testVideoPath = "E:\\WEEK4_01.mp4";

    @Test
    void extractBasicDTO() {
        try {
            // 파일로 open
            File f = new File(testVideoPath);

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(testVideoPath);

            int timeSec = (int) (videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT) / videoCapture.get(Videoio.CAP_PROP_FPS));

            Time length = TimeConverter.convert(timeSec);
            Long videoSize = f.length();
            VideoType videoType = VideoTypeAttributeConverter.convert(FilenameUtils.getExtension(f.getName()));
            VideoFrame videoFrame = VideoFrameAttributeConverter.convert(videoCapture.get(Videoio.CAP_PROP_FPS));

            assertThat(length).isEqualTo(new Time(00, 17, 24));
            assertThat(videoSize).isEqualTo(134639720);
            assertThat(videoType).isEqualTo(VideoType.MP4);
            assertThat(videoFrame).isEqualTo(VideoFrame.FPS30);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}