package org.zhuyuqinlan.lemall.common.file.dto.ext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;

@Getter
@Setter
@Schema(description = "文件上传信息")
public class FileInfoExistDTO extends FileInfoCacheByMd5DTO {

    @Schema(description = "文件是否存在")
    private boolean exist;

    @Schema(description = "上传凭证")
    private String uploadId;
}
