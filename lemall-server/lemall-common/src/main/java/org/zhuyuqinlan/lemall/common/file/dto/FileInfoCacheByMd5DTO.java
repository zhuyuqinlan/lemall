package org.zhuyuqinlan.lemall.common.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class FileInfoCacheByMd5DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;                  // 文件ID
    private String storageType;       // 存储类型 MINIO/LOCAL/OSS
    private String bucket;            // 存储桶名称
    private String uri;               // 对象名/路径
    private String originalName;      // 原始文件名
    private Long size;                // 文件大小
    private String contentType;       // MIME类型
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
}
