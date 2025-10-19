package org.zhuyuqinlan.lemall.common.file.service;

import java.util.Map;

/**
 * 云存储扩展接口
 * 提供前端直传等云存储特有功能
 */
public interface CloudFileStorageService extends FileStorageService {

    /**
     * 生成前端直传临时凭证（POST Policy）
     * @param objectPrefix 对象前缀
     * @param expireSeconds 过期时间（秒）
     * @return 前端上传所需参数
     */
    Map<String, Object> getPostPolicy(String objectPrefix, int expireSeconds);

    /**
     * 前端直传后回调
     * @param originalFileName 路径
     * @return url
     */
    String saveFileRecord(String originalFileName);
}
