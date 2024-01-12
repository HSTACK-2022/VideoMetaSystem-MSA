package org.hstack.vmeta.extraction.basic;

import org.apache.commons.io.FilenameUtils;
import org.hstack.vmeta.extraction.basic.time.TimeConverter;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrame;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrameAttributeConverter;
import org.hstack.vmeta.extraction.basic.videoType.VideoType;
import org.hstack.vmeta.extraction.basic.videoType.VideoTypeAttributeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.InputMismatchException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest
class BasicExtractionServiceTest {

    String testVideoPath = "E:\\WEEK4_01.mp4";
    String testFailVideoPath = "E:\\WEEK4_01";

    @Test
    @DisplayName("Basic MD 추출 - 성공")
    void extractBasicDTO() {
        try {
            // 파일로 open
            File f = new File(testVideoPath);

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(testVideoPath);

            int timeSec = (int) (videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT) / videoCapture.get(Videoio.CAP_PROP_FPS));

            Time length = TimeConverter.convert2Time(timeSec);
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

    @Test
    @DisplayName("Basic MD 추출 - 실패 : 잘못된 fps")
    void extractBasicDTO_Fail_WrongFps() throws Exception {
        try {

            VideoFrame videoFrame = VideoFrameAttributeConverter.convert(10.0);

            if (videoFrame == null) {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
            return;
        }
        failBecauseExceptionWasNotThrown(InputMismatchException.class);
    }

    @Test
    @DisplayName("Basic MD 추출 - 실패 : 잘못된 파일 경로")
    void extractBasicDTO_Fail_WrongPath() throws Exception {
        try {
            // 파일로 open
            File f = new File(testFailVideoPath);
            String ext = FilenameUtils.getExtension(f.getName());
            VideoType videoType = VideoTypeAttributeConverter.convert(ext);

            if (videoType == null) {
                throw new FileNotFoundException();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        failBecauseExceptionWasNotThrown(FileNotFoundException.class);
    }
}