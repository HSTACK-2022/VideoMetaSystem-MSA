package org.hstack.vmeta.extraction.audio;


import org.hstack.vmeta.extraction.basic.time.TimeConverter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class SttCalculator {

    @Value("${etri.keys}")
    private String[] ETRI_API_KEY_SIZE;

    /*
     * filePath에 해당하는 비디오 파일에 대해 SttThread를 호출한다.
     * @param
     * - filePath : 음성 파일 경로
     * @returnVal
     * - List<Script>
     */
    public List<AudioDTO.Script> getScriptList(String filePath) {
        try {
            // 스레드 풀 생성
            int keySize = ETRI_API_KEY_SIZE.length;
            ExecutorService threadPool = Executors.newFixedThreadPool(keySize);

            // Key의 개수만큼 스레드 생성 및 초기화
            SttThread[] sttThreads = new SttThread[keySize];
            for (int i = 0; i < keySize; i++) {
                sttThreads[i] = new SttThread(ETRI_API_KEY_SIZE[i]);
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
            //while(!threadPool.isTerminated())
            threadPool.awaitTermination(10, TimeUnit.MINUTES);
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

            return scriptList;

        } catch (InterruptedException ie) {
            // TODO : logging
            ie.printStackTrace();
            return null;
        }
    }

}

