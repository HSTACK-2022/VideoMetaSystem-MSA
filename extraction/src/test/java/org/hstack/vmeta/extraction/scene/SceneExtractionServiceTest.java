package org.hstack.vmeta.extraction.scene;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.io.File;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SceneExtractionServiceTest {

    @InjectMocks
    private SceneExtractionService sceneExtractionService;

    private static final String filePath = "E:\\test\\WEEK4_01.mp4";;

    @Test
    @DisplayName("장면 프레임 추출 - 정상실행")
    void extractFrameImage() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   // 라이브러리 로딩

        // given
        String expectDirPath = Paths.get(filePath).getParent().toString()
                + File.separator
                + "frames";
        File expectDir = new File(expectDirPath);

        // when
        sceneExtractionService.extractFrameImage(filePath);

        // then
        assertTrue(expectDir.exists());
        assertTrue(expectDir.isDirectory());
        assertThat(expectDir.list().length).isNotEqualTo(0);
    }
}