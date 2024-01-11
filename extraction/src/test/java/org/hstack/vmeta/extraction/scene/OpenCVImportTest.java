package org.hstack.vmeta.extraction.scene;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVImportTest {

    @Test
    void openCVTest() {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        System.out.println("Welcome to OpenCV " + Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("m = " + m.dump());
    }

}
