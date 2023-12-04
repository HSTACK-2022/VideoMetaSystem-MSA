package org.hstack.vmeta.videoMetadata.metadata.videoType;

import jakarta.persistence.AttributeConverter;

public class VideoTypeAttributeConverter implements AttributeConverter<VideoType, String> {

    @Override
    public String convertToDatabaseColumn(VideoType v) {
        String res;
        switch (v) {
            case MP4 -> res = "MP4";
            case AVI -> res = "AVI";
            default -> res = null;
        }
        return res;
    }

    @Override
    public VideoType convertToEntityAttribute(String s) {
        if (s.equals("MP4")) {
            return VideoType.MP4;
        } else if (s.equals("AVI")) {
            return VideoType.AVI;
        }
        return null;
    }
}

