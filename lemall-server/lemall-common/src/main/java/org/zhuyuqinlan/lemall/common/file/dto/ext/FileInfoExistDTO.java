package org.zhuyuqinlan.lemall.common.file.dto.ext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "文件上传信息")
public class FileInfoExistDTO extends FileInfoCacheByMd5DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件是否存在")
    private boolean exist;

    @Schema(description = "上传凭证")
    private String uploadCode;
}
