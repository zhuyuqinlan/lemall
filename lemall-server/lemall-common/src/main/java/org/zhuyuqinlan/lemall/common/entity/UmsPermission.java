package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 后台用户权限表
    */
@TableName(value = "ums_permission")
public class UmsPermission {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父级权限id
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 权限值
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 前端资源路径
     */
    @TableField(value = "uri")
    private String uri;

    /**
     * 启用状态；0->禁用；1->启用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 排序
     */
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
     * 获取父级权限id
     *
     * @return pid - 父级权限id
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置父级权限id
     *
     * @param pid 父级权限id
     */
    public void setPid(Long pid) {
        this.pid = pid;
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
     * 获取权限值
     *
     * @return value - 权限值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置权限值
     *
     * @param value 权限值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     *
     * @return type - 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     *
     * @param type 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取前端资源路径
     *
     * @return uri - 前端资源路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置前端资源路径
     *
     * @param uri 前端资源路径
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 获取启用状态；0->禁用；1->启用
     *
     * @return status - 启用状态；0->禁用；1->启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置启用状态；0->禁用；1->启用
     *
     * @param status 启用状态；0->禁用；1->启用
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}