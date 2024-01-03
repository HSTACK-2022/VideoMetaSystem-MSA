package org.hstack.vmeta.extraction.basic.videoType;

public class VideoTypeAttributeConverter {

    public static VideoType convert(String ext) {
        String s = ext.toUpperCase();
        if (s.equals("MP4")) {
            return VideoType.MP4;
        } else if (s.equals("AVI")) {
            return VideoType.AVI;
        }
        return null;
    }
}

