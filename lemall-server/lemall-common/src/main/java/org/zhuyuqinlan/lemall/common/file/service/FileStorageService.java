package org.zhuyuqinlan.lemall.common.file.service;

import java.io.InputStream;

/**
 * 基础文件存储接口
 * 本地存储和云存储都需要实现
 */
public interface FileStorageService {

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param size 文件大小
     * @param objectName 文件uri
     * @param contentType MIME类型
     * @return 存储后的对象名或 URL
     */
    String uploadFile(String objectName, InputStream inputStream, long size, String contentType);

    /** 删除文件 */
    void deleteFile(String objectName);

    /** 下载文件 **/
    InputStream downloadFile(String objectName);

    /** 获取文件访问 URL（带签名或直链） */
    String getFileUrl(String objectName);
}
