package org.zhuyuqinlan.lemall.common.file.constant;

/**
 * 文件常量
 */
public interface FileStorageConstant {
    // 数据库存的minio名称
    String MINIO_TYPE = "minio";
    // 数据库存的本地类型名称
    String LOCAL_TYPE = "local";
    // minio上传临时令牌过期时间（秒）
    int POST_POLICY_EXPIRE  = 300;
    // 本地文件access过期时间（秒）
    int LOCAL_ACCESS_EXPIRE = 30;
    // 本地文件上传凭证过期时间（秒）
    int LOCAL_UPLOAD_EXPIRE = 300;
}
