package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
 * 后台资源表
 */
@TableName(value = "ums_resource")
public class UmsResource {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 资源名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 资源URL
     */
    @TableField(value = "url")
    private String url;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 资源分类ID
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取资源名称
     *
     * @return name - 资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源名称
     *
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取资源URL
     *
     * @return url - 资源URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置资源URL
     *
     * @param url 资源URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取资源分类ID
     *
     * @return category_id - 资源分类ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置资源分类ID
     *
     * @param categoryId 资源分类ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}