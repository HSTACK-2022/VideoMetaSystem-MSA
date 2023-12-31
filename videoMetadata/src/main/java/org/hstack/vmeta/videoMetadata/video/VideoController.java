package org.hstack.vmeta.videoMetadata.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    private static String logMessage(String funcName, Long id, boolean result) {
        return "[video] " + funcName + " #" + id + " : " + result;
    }

    @GetMapping
    public ResponseEntity<?> getAllVideo() {
        try {
            List<VideoDTO> videoDTOs = videoService.getAll();
            return ResponseEntity.ok(videoDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveVideo(@RequestBody VideoDTO videoDTO) {
        try {
            Long videoId = videoService.save(videoDTO);
            String body = logMessage("save", videoId, true);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteVideo(@RequestParam Long videoId) {
        try {
            videoService.delete(videoId);
            String body = logMessage("delete", videoId, true);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
