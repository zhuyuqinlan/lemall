package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 统一文件存储表
 */
@TableName(value = "fs_file_storage")
public class FsFileStorage {
    /**
     * 文件ID，主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 存储类型：MINIO, LOCAL, OSS等
     */
    @TableField(value = "storage_type")
    private String storageType;

    /**
     * 存储桶名称，可选，针对对象存储
     */
    @TableField(value = "bucket")
    private String bucket;

    /**
     * 文件路径/对象名
     */
    @TableField(value = "uri")
    private String uri;

    /**
     * 文件key
     */
    @TableField(value = "file_key")
    private String fileKey;

    /**
     * 文件大小，单位字节
     */
    @TableField(value = "`size`")
    private Long size;

    /**
     * MIME类型，如 image/png, application/pdf
     */
    @TableField(value = "content_type")
    private String contentType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 文件MD5，用于去重和完整性校验
     */
    @TableField(value = "md5")
    private String md5;

    /**
     * 获取文件ID，主键
     *
     * @return id - 文件ID，主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置文件ID，主键
     *
     * @param id 文件ID，主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取存储类型：MINIO, LOCAL, OSS等
     *
     * @return storage_type - 存储类型：MINIO, LOCAL, OSS等
     */
    public String getStorageType() {
        return storageType;
    }

    /**
     * 设置存储类型：MINIO, LOCAL, OSS等
     *
     * @param storageType 存储类型：MINIO, LOCAL, OSS等
     */
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    /**
     * 获取存储桶名称，可选，针对对象存储
     *
     * @return bucket - 存储桶名称，可选，针对对象存储
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * 设置存储桶名称，可选，针对对象存储
     *
     * @param bucket 存储桶名称，可选，针对对象存储
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * 获取文件路径/对象名
     *
     * @return uri - 文件路径/对象名
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置文件路径/对象名
     *
     * @param uri 文件路径/对象名
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 获取文件key
     *
     * @return file_key - 文件key
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * 设置文件key
     *
     * @param fileKey 文件key
     */
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    /**
     * 获取文件大小，单位字节
     *
     * @return size - 文件大小，单位字节
     */
    public Long getSize() {
        return size;
    }

    /**
     * 设置文件大小，单位字节
     *
     * @param size 文件大小，单位字节
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 获取MIME类型，如 image/png, application/pdf
     *
     * @return content_type - MIME类型，如 image/png, application/pdf
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 设置MIME类型，如 image/png, application/pdf
     *
     * @param contentType MIME类型，如 image/png, application/pdf
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取文件MD5，用于去重和完整性校验
     *
     * @return md5 - 文件MD5，用于去重和完整性校验
     */
    public String getMd5() {
        return md5;
    }

    /**
     * 设置文件MD5，用于去重和完整性校验
     *
     * @param md5 文件MD5，用于去重和完整性校验
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }
}