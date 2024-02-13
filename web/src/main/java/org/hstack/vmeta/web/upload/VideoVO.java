package org.hstack.vmeta.web.upload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoVO {

    String fileName;
    String filePath;
    String uploaderName;
}
