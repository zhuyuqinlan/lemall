package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 后台用户角色表
    */
@TableName(value = "ums_role")
public class UmsRole {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 后台用户数量
     */
    @TableField(value = "admin_count")
    private Integer adminCount;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 启用状态：0->禁用；1->启用
     */
    @TableField(value = "`status`")
    private Integer status;

    @TableField(value = "sort")
    private Integer sort;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取后台用户数量
     *
     * @return admin_count - 后台用户数量
     */
    public Integer getAdminCount() {
        return adminCount;
    }

    /**
     * 设置后台用户数量
     *
     * @param adminCount 后台用户数量
     */
    public void setAdminCount(Integer adminCount) {
        this.adminCount = adminCount;
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
     * 获取启用状态：0->禁用；1->启用
     *
     * @return status - 启用状态：0->禁用；1->启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置启用状态：0->禁用；1->启用
     *
     * @param status 启用状态：0->禁用；1->启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}