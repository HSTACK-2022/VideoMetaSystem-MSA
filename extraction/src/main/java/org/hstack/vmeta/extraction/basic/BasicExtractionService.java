package org.hstack.vmeta.extraction.basic;

import org.apache.commons.io.FilenameUtils;
import org.hstack.vmeta.extraction.basic.time.TimeConverter;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrame;
import org.hstack.vmeta.extraction.basic.videoFrame.VideoFrameAttributeConverter;
import org.hstack.vmeta.extraction.basic.videoType.VideoType;
import org.hstack.vmeta.extraction.basic.videoType.VideoTypeAttributeConverter;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Time;

@Service
public class BasicExtractionService {

    public BasicDTO extractBasicDTO(String filePath) {

        try {
            // 파일로 open
            File f = new File(filePath);

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(filePath);

            int timeSec = (int) (videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT) / videoCapture.get(Videoio.CAP_PROP_FPS));

            Time length = TimeConverter.convert(timeSec);
            Long videoSize = f.length();
            VideoType videoType = VideoTypeAttributeConverter.convert(FilenameUtils.getExtension(f.getName()));
            VideoFrame videoFrame = VideoFrameAttributeConverter.convert(videoCapture.get(Videoio.CAP_PROP_FPS));

            return BasicDTO.builder()
                    .length(length)
                    .videoSize(videoSize)
                    .videoType(videoType)
                    .videoFrame(videoFrame)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}
