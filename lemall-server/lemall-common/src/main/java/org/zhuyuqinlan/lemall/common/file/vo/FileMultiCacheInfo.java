package org.zhuyuqinlan.lemall.common.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(description = "缓存的文件信息")
public class FileMultiCacheInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "上传id")
    private String uploadId;

    @Schema(description = "md5")
    private String md5;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "文件key")
    private String fileKey;

    @Schema(description = "文件大小")
    private long size;

    @Schema(description = "已上传分片编号")
    private List<Integer> partNums;
}
