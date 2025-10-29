package org.zhuyuqinlan.lemall.common.file.dto.ext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(description = "分片凭证")
public class MultipartUploadInfo extends FileInfoCacheByMd5DTO {

    @Schema(description = "文件是否存在")
    private boolean exist;

    @Schema(description = "上传id")
    private String uploadId;

    @Schema(description = "分片列表，每个分片包含 partNumber 和 url")
    private List<Map<String, Object>> parts;

    @Schema(description = "总分片数")
    private int partCount;
}
