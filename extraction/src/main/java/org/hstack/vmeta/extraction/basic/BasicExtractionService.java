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
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.InputMismatchException;

@Service
public class BasicExtractionService {

    /*
     * 영상 기본 정보 추출
     * @param
     * - filePath : 영상 파일 경로
     * @returnVal
     * - length, videoSize, videoType(ext), videoFrame(fps)
     */
    public BasicDTO extractBasicDTO(String filePath) {

        try {
            // return val
            Time length;
            Long videoSize;
            VideoType videoType;
            VideoFrame videoFrame;

            // 파일로 open
            File f = new File(filePath);
            String ext = FilenameUtils.getExtension(f.getName());
            videoType = VideoTypeAttributeConverter.convert(ext);

            // exception : wrong file path
            if (videoType == null) {
                throw new FileNotFoundException();
            }

            // videoCapture로 open
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture videoCapture = new VideoCapture();
            videoCapture.open(filePath);

            int timeSec = (int) (videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT) / videoCapture.get(Videoio.CAP_PROP_FPS));
            length = TimeConverter.convert2Time(timeSec);

            videoSize = f.length();
            videoFrame = VideoFrameAttributeConverter.convert(videoCapture.get(Videoio.CAP_PROP_FPS));

            // exception : wrong fps
            if (videoFrame == null) {
                throw new InputMismatchException();
            }

            return BasicDTO.builder()
                    .length(length)
                    .videoSize(videoSize)
                    .videoType(videoType)
                    .videoFrame(videoFrame)
                    .build();

        } catch(FileNotFoundException fe) {
            // TODO : Logging
            fe.printStackTrace();
        } catch (InputMismatchException ie) {
            // TODO : Logging
            ie.printStackTrace();
        } catch (Exception e) {
            // TODO : Logging
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}
