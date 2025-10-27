package org.zhuyuqinlan.lemall.common.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(description = "秒传返回类")
public class FileInfoCacheByMd5DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    private Long id;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "存储桶名称")
    private String bucket;

    @Schema(description = "文件访问URL")
    private String url;

    @Schema(description = "文件key")
    private String fileKey;

    @Schema(description = "文件大小，单位字节")
    private Long size;

    @Schema(description = "MIME类型")
    private String contentType;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
