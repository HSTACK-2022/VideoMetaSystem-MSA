package org.hstack.vmeta.web.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestPart("multipartFile") MultipartFile file) {
        
        // 1. 입력 받은 파일 저장
        String filePath = uploadService.saveFile(file);
        if (filePath == null) {
            // TODO : Error처리 - 상태 코드 맞춰서 제대로 하기
            return ResponseEntity.status(400).build();
        }
        
        // 2. 파일 정보 DB 저장

    }
}
