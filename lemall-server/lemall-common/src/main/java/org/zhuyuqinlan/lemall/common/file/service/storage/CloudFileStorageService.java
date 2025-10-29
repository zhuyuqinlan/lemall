package org.zhuyuqinlan.lemall.common.file.service.storage;

import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;

import java.io.InputStream;

/**
 * 云存储扩展接口
 * 提供前端直传等云存储特有功能
 */
public interface CloudFileStorageService {

    /**
     * 上传文件
     *
     * @param inputStream 文件流
     * @param size        文件大小
     * @param fileKey     文件实际路径
     * @param contentType MIME类型
     * @return 存储后的对象名或 URL
     */
    FileInfoDTO uploadFile(String fileKey, InputStream inputStream, long size, String contentType, String md5, boolean isPublic);

    /**
     * 删除文件
     */
    void deleteFile(String fileKey, boolean isPublic);

    /**
     * 下载文件
     **/
    InputStream downloadFile(String fileKey, boolean isPublic);

    /**
     * 获取文件访问 URL（带签名或直链）
     */
    String getFileUrl(String fileKey, boolean isPublic);

    /**
     * 生成前端直传临时凭证（POST Policy）
     *
     * @param fileKey       文件key
     * @param expireSeconds 过期时间（秒）
     * @param isPublic      是否为公共桶
     * @return 前端上传所需参数
     */
    MultipartUploadInfo getPostPolicy(String fileKey, int expireSeconds, String uploadId, boolean isPublic);


    FileInfoDTO complete(String fileKey, String contentType, String md5, boolean isPublic);

    /**
     * 生成前端分片直传临时凭证
     *
     * @param fileKey       文件key
     * @param expireSeconds 过期时间（秒）
     * @param partSize      分片大小
     * @param fileSize      文件大小
     * @param isPublic      是否为公共桶
     * @return 前端上传所需参数
     */
    MultipartUploadInfo getMultipartUploadInfo(String fileKey, long partSize, long fileSize, int expireSeconds, String uploadId, boolean isPublic);

    /**
     * 前端直传后回调
     */
    FileInfoDTO mergeMultipartUpload(String uploadId, String fileKey, String md5, Long size, String contentType, boolean isPublic);



}
