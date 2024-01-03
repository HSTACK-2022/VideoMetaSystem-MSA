package org.hstack.vmeta.extraction.basic.videoFrame;

public class VideoFrameAttributeConverter {
    public static VideoFrame convert(Double d) {
        int fps = (int) Math.floor(d);
        switch(fps) {
            case 24:
                return VideoFrame.FPS24;
            case 30:
                return VideoFrame.FPS30;
            default:
                return null;
        }
    }
}
