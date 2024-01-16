package org.hstack.vmeta.extraction.audio;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FFmpegCalculator {

    @Value("${ffmpeg.location}")
    private String ffmpegPath;

    @Value("${ffprobe.location}")
    private String ffprobePath;

    private static final String SECORIGIN = "10";

    public boolean videoPreprocessing(String filePath) {
        try {

            // make audio dir
            String dirPath = Paths.get(filePath).getParent().toString()
                    + File.separator
                    + "audio";
            Files.createDirectory(Paths.get(dirPath));

            // do logic
            String aduioFilePath = convertVideo2Audio(filePath);
            splitAudioWithSec(aduioFilePath);

            return true;

        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    private String convertVideo2Audio(String filePath) throws Exception {

        String dirPath = Paths.get(filePath).getParent().toString() + File.separator + "audio";
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

        return expectFilePath;
    }

    private void splitAudioWithSec(String audioFilepath) throws Exception {

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
            System.out.println(audioNum);
        }
    }
}
