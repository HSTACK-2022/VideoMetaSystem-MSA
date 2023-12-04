package org.hstack.vmeta.videoMetadata.metadata.videoFrame;

import jakarta.persistence.AttributeConverter;
import org.hstack.vmeta.videoMetadata.metadata.videoType.VideoType;

public class VideoFrameAttributeConverter implements AttributeConverter<VideoFrame, String> {

    @Override
    public String convertToDatabaseColumn(VideoFrame v) {
        String res;
        switch (v) {
            case FPS24 -> res = "FPS24";
            case FPS30 -> res = "AVI";
            default -> res = null;
        }
        return res;
    }

    @Override
    public VideoFrame convertToEntityAttribute(String s) {
        if (s.equals("FPS24")) {
            return VideoFrame.FPS24;
        } else if (s.equals("FPS30")) {
            return VideoFrame.FPS30;
        }
        return null;
    }
}
