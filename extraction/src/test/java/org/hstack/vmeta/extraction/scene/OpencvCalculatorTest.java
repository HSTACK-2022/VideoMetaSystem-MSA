package org.hstack.vmeta.extraction.scene;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OpencvCalculatorTest {

    String imgPath1 = "E:\\test\\testImg (1).jpg";
    String imgPath2 = "E:\\test\\testImg (2).jpg";

    @Test
    @DisplayName("두 프레임의 PSNR비 계산")
    void getPSNR() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        Mat m1 = Imgcodecs.imread(imgPath1);
        Mat m2 = Imgcodecs.imread(imgPath2);

        double diff1 = OpencvCalculator.getPSNR(m1, m1);
        double diff2 = OpencvCalculator.getPSNR(m1, m2);

        assertTrue(Double.isInfinite(diff1));   // 같은 프레임의 PSNR은 최대치
        assertFalse(Double.isInfinite(diff2));  // 다른 프레임의 PSNR은 최대치가 아님
    }

    @Test
    @DisplayName("새로운 프레임 저장")
    void saveImage() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        Mat m = Imgcodecs.imread(imgPath1);
        String dirPath = System.getProperty("user.dir");    // 현재 작업 영역으로 설정
        OpencvCalculator.saveImage(m, dirPath, 0L);

        String expectFilePath = dirPath + File.separator + "0.png";
        System.out.println(expectFilePath);
        assertThat(new File(expectFilePath).exists()).isEqualTo(true);
    }
}