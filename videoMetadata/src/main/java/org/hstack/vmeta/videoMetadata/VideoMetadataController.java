package org.hstack.vmeta.videoMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videoMetadata")
public class VideoMetadataController {

    @Autowired
    private VideoMetadataService videoMetadataService;

    @GetMapping
    public ResponseEntity<?> getFilteredVideoMetadata(
            @RequestParam String title,
            @RequestParam String uploaderName,
            @RequestParam String keyword,
            @RequestParam String categoryType)
    {
        try {
            List<VideoMetadataDTO> resultDTOs = videoMetadataService.getByFilter(
                    title, uploaderName, keyword, categoryType);
            return ResponseEntity.ok(resultDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
