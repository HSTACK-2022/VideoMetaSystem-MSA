package org.hstack.vmeta.extraction.audio;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FFmpegCalculatorTest {

    private static final String filePath = "E:\\test\\WEEK4_01.mp4";;
    private static final String audioFilepath = "E:\\test\\audio\\extracted.wav";;

    @Value("${ffmpeg.location}")
    private String ffmpegPath;

    @Value("${ffprobe.location}")
    private String ffprobePath;

    private final String SECORIGIN = "10";


    @Test
    @DisplayName("실행 전 폴더 생성")
    void videoPreprocessing() {
        try {
            // given
            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "audio";

            // when
            Files.createDirectory(Paths.get(dirPath));

            // then
            assertTrue(new File(dirPath).exists());
            assertTrue(new File(dirPath).isDirectory());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("영상 -> 음원 추출")
    void convertVideo2Audio() {
        try {

            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "audio";
            String expectFilePath = dirPath + File.separator + "extracted.wav";

            //ffmpeg -y -i <infile> -vn -ac 1 -ar 16k -ab 128k <outfile>

            FFmpegBuilder builder = new FFmpegBuilder()
                    .overrideOutputFiles(true)
                    .setInput(filePath)
                    .addOutput(expectFilePath)
                    .addExtraArgs("-y")
                    .addExtraArgs("-vn")
                    .addExtraArgs("-ac","1")
                    .addExtraArgs("-ar","16k")
                    .addExtraArgs("-ab","128k")
                    .addExtraArgs("-acodec","pcm_s16le")
                    .done();

            // FFmpeg 실행
            FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath), new FFprobe(ffprobePath));
            executor.createJob(builder).run();

            // then
            File f = new File(expectFilePath);
            assertTrue(f.exists());
            assertTrue(f.isFile());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    @DisplayName("10초 단위로 자르기")
    void splitAudioWithSec() {
        try {
            String dirPath = Paths.get(audioFilepath).getParent().toString();

            //ffmpeg -y -i <infile> -vn -ac 1 -ar 16k -ab 128k <outfile>
            FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath), new FFprobe(ffprobePath));

            // 파일 길이 얻기
            FFmpegProbeResult probeResult = new FFprobe(ffprobePath).probe(audioFilepath);
            double audioLen = probeResult.getFormat().duration; // 초단위

            int audioNum = 0;
            double endOfTime = Math.ceil(audioLen);
            for (double i = 0; i < endOfTime; i += 10) {
                String startTime = (i == 0) ? "0" : (i + 1) + "";

                FFmpegBuilder builder = new FFmpegBuilder()
                        .overrideOutputFiles(true)
                        .setInput(audioFilepath)
                        .addOutput(dirPath + File.separator + audioNum++ + ".wav")
                        .addExtraArgs("-ss", startTime)
                        .addExtraArgs("-t", SECORIGIN)
                        .addExtraArgs("-acodec", "copy")
                        .done();

                // FFmpeg 실행
                executor.createJob(builder).run();
            }

            // then
            File f = new File(dirPath);
            assertThat(f.list().length).isEqualTo(audioNum + 1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}