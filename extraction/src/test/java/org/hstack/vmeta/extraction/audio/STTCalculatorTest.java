package org.hstack.vmeta.extraction.audio;

import org.hstack.vmeta.extraction.basic.time.TimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class STTCalculatorTest {


    @Value("${etri.keys}")
    private String[] ETRI_API_KEY;

    private static final String filePath = "E:\\test\\WEEK4_01.mp4";

    @Test
    @DisplayName("멀티스레딩으로 STT 실행")
    void getScriptList() {

        try {

            // 스레드 풀 생성
            int keySize = ETRI_API_KEY.length;
            ExecutorService threadPool = Executors.newFixedThreadPool(keySize);

            // Key의 개수만큼 스레드 생성 및 초기화
            SttThread[] sttThreads = new SttThread[keySize];
            for (int i = 0; i < keySize; i++) {
                sttThreads[i] = new SttThread(ETRI_API_KEY[i]);
            }

            // filePath에서 audioDir 경로 얻기
            String dirPath = Paths.get(filePath).getParent().toString() + File.separator + "audio";
            int fileCnt = new File(dirPath).listFiles().length;

            // audioFile을 각 스레드로 배분
            for(int i = 0; i < fileCnt - 1; i++) {
                String audioFilePath = dirPath + File.separator + i + ".wav";
                sttThreads[i % keySize].pushFilePath(audioFilePath);
            }

            // 멀티스레드 실행
            for (SttThread st : sttThreads) {
                threadPool.execute(st);
            }

            // 스레드 종료
            while(!threadPool.isTerminated())
            threadPool.shutdown();

            // 반환 값 초기화
            List<AudioDTO.Script> scriptList = new ArrayList<>();
            for (SttThread st : sttThreads) {
                Map<Integer, String> scriptMap = st.getThreadResult();  // 스레드별 결과값 얻기
                for (int key : scriptMap.keySet()) {                    // 해당 결과를 Script 형태로 변환 저장
                    Time time = TimeConverter.convert2Time(key*10);
                    scriptList.add(
                            AudioDTO.Script.builder()
                                    .time(time)
                                    .content(scriptMap.get(key))
                                    .build()
                    );
                }
            }

            Collections.sort(scriptList, Comparator.comparing(s -> s.time));

            // TODO : Logging
            for (AudioDTO.Script s : scriptList) {
                System.out.println( s.time + " " + s.content);
            }

            assertThat(scriptList).isNotEqualTo(0);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}