package org.hstack.vmeta.web.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class UploadService {

    @Value("${upload.path}")
    private String UPLOAD_ROOT;

    /*
     * [saveFile]
     *  > 전달받은 file이 비디오인지 확인 후 저장
     * @param
     * - file : 전달받은 파일
     * @returnVal
     * - newFilePath : 정상 저장 된 경우 해당 경로
     * - null : 오류 발생의 경우 null
     */
    public String saveFile(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String fileExt = StringUtils.getFilenameExtension(originFileName);

        if (!StringUtils.hasText(contentType)) {
            return null;
        } else if (!contentType.equals("video/mpeg")) {
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String uuidFileName = uuid + "." + fileExt;    // 새로운 파일명
        String newFilePath = UPLOAD_ROOT + File.separator + uuid + File.separator + uuidFileName;

        try {
            Files.copy(file.getInputStream(), Path.of(newFilePath));
            return newFilePath;
        } catch (Exception e) {
            return null;
        }
    }
}
