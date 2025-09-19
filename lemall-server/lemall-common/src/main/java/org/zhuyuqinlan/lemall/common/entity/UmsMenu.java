package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 后台菜单表
    */
@TableName(value = "ums_menu")
public class UmsMenu {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父级ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 菜单名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 菜单级数
     */
    @TableField(value = "`level`")
    private Integer level;

    /**
     * 菜单排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 前端名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 前端图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 前端隐藏
     */
    @TableField(value = "hidden")
    private Integer hidden;

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
     * 获取父级ID
     *
     * @return parent_id - 父级ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级ID
     *
     * @param parentId 父级ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
     * 获取菜单名称
     *
     * @return title - 菜单名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置菜单名称
     *
     * @param title 菜单名称
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取菜单级数
     *
     * @return level - 菜单级数
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置菜单级数
     *
     * @param level 菜单级数
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取菜单排序
     *
     * @return sort - 菜单排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置菜单排序
     *
     * @param sort 菜单排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取前端名称
     *
     * @return name - 前端名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置前端名称
     *
     * @param name 前端名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取前端图标
     *
     * @return icon - 前端图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置前端图标
     *
     * @param icon 前端图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取前端隐藏
     *
     * @return hidden - 前端隐藏
     */
    public Integer getHidden() {
        return hidden;
    }

    /**
     * 设置前端隐藏
     *
     * @param hidden 前端隐藏
     */
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }
}