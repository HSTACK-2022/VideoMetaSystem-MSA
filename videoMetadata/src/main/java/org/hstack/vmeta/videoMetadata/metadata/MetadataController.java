package org.hstack.vmeta.videoMetadata.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    private static String logMessage(String funcName, Long id, boolean result) {
        return "[metadata] " + funcName + " #" + id + " : " + result;
    }

    @PostMapping
    public ResponseEntity<?> saveMetadata(@RequestBody MetadataDTO metadataDTO) {
        try{
            Long videoId = metadataService.save(metadataDTO);
            String body = logMessage("save", videoId, true);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMetadata(@RequestBody MetadataDTO metadataDTO) {
        try{
            Long videoId = metadataService.update(metadataDTO);
            String body = logMessage("update", videoId, true);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteMetadata(@RequestParam Long videoId) {
        try {
            metadataService.delete(videoId);
            String body = logMessage("delete", videoId, true);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
