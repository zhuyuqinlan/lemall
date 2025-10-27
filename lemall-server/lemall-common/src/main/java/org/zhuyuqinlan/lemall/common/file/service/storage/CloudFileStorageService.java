package org.zhuyuqinlan.lemall.common.file.service.storage;

import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;

import java.io.InputStream;
import java.util.Map;

/**
 * 云存储扩展接口
 * 提供前端直传等云存储特有功能
 */
public interface CloudFileStorageService {

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param size 文件大小
     * @param objectName 文件uri
     * @param contentType MIME类型
     * @return 存储后的对象名或 URL
     */
    FileInfoDTO uploadFile(String objectName, InputStream inputStream, long size, String contentType, String md5, boolean isPublic);

    /** 删除文件 */
    void deleteFile(String objectName, boolean isPublic);

    /** 下载文件 **/
    InputStream downloadFile(String objectName, boolean isPublic);

    /** 获取文件访问 URL（带签名或直链） */
    String getFileUrl(String objectName, boolean isPublic);

    /**
     * 生成前端直传临时凭证（POST Policy）
     * @param objectName 对象名
     * @param expireSeconds 过期时间（秒）
     * @param isPublic 是否为公共桶
     * @return 前端上传所需参数
     */
    Map<String, Object> getPostPolicy(String objectName, int expireSeconds, boolean isPublic);

    /**
     * 前端直传后回调
     * @param objectName 路径
     * @return url
     * @param isPublic 是否为公共桶
     */
    Map<String,String> saveFileRecord(String objectName, boolean isPublic);
}
