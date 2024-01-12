package org.hstack.vmeta.extraction.scene;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

class OpencvCalculator {

    public static double getPSNR(Mat m1, Mat m2) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        Mat diff = new Mat();
        Core.absdiff(m1, m2, diff);         // m1과 m2의 차이를 diff에 저장
        Core.multiply(diff, diff, diff);    // SSE 추출을 위해 diff^2 수행
        Scalar s = Core.sumElems(diff);     // 단일 색상으로 표현

        double sse = 0;
        for (double d : s.val) {
            sse += d;
        }

        double mse = sse / (3 * m1.rows() * m2.cols());     // MSE 계산
        return 10.0 * Math.log10((255*255) / mse);          // PSNR 반환
    }

    public static void saveImage(Mat m, String dirPath, long timeSec) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        String filePath = dirPath + File.separator + timeSec + ".png";
        Imgcodecs.imwrite(filePath, m);
    }
}
