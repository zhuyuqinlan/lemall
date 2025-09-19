package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 后台用户表
    */
@TableName(value = "ums_admin")
public class UmsAdmin {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "`password`")
    private String password;

    /**
     * 头像
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 备注信息
     */
    @TableField(value = "note")
    private String note;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后登录时间
     */
    @TableField(value = "login_time")
    private Date loginTime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    @TableField(value = "`status`")
    private Integer status;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取头像
     *
     * @return icon - 头像
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置头像
     *
     * @param icon 头像
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取备注信息
     *
     * @return note - 备注信息
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注信息
     *
     * @param note 备注信息
     */
    public void setNote(String note) {
        this.note = note;
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
     * 获取最后登录时间
     *
     * @return login_time - 最后登录时间
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置最后登录时间
     *
     * @param loginTime 最后登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取帐号启用状态：0->禁用；1->启用
     *
     * @return status - 帐号启用状态：0->禁用；1->启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置帐号启用状态：0->禁用；1->启用
     *
     * @param status 帐号启用状态：0->禁用；1->启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}