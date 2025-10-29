package org.zhuyuqinlan.lemall.common.file.constant;

/**
 * 文件常量
 */
public interface FileStorageConstant {

    // 数据库存的本地类型名称
    String LOCAL_TYPE = "local";

    // 本地文件access过期时间（秒）
    int LOCAL_ACCESS_EXPIRE = 30;

    // 本地文件上传凭证过期时间（秒）
    int LOCAL_UPLOAD_EXPIRE = 300;

    // 数据库存的minio名称
    String MINIO_TYPE = "minio";

    // minio access过期时间（秒）
    int MINIO_ACCESS_EXPIRE = 30;

    // minio上传临时令牌过期时间（秒）
    int POST_POLICY_EXPIRE  = 300;

    // minio分片上传临时令牌过期时间（秒）
    int POST_POLICY_MULTIPART_EXPIRE = 10800;

    // 分片大小(字节)
    long FILE_FRAGMENT_SIZE = 5242880; // 5MB

    // 分片上传最小文件
    long MINIMUM_THRESHOLD_FOR_FILE_FRAGMENTATION = 52428800; // 50 MB
}
