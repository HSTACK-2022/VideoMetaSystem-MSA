package org.hstack.vmeta.extraction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/extraction")
public class ExtractionController {

    @Autowired
    private ExtractionService extractionService;

    @PostMapping
    public ResponseEntity<?> extractMetadata(@RequestBody ExtractionVO extractionVO) {
        try {
            Long id = extractionVO.getId();
            String title = extractionVO.getTitle();
            String filePath = extractionVO.getFilePath();
            MetadataDTO metadataDTO = extractionService.extractMetadataDTO(id, title, filePath);
            return ResponseEntity.ok(HttpEntity.EMPTY);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
