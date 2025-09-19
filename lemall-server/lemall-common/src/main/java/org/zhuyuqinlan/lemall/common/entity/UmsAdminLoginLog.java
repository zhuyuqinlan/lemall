package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 后台用户登录日志表
    */
@TableName(value = "ums_admin_login_log")
public class UmsAdminLoginLog {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "admin_id")
    private Long adminId;

    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "address")
    private String address;

    /**
     * 浏览器登录类型
     */
    @TableField(value = "user_agent")
    private String userAgent;

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
     * @return admin_id
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取浏览器登录类型
     *
     * @return user_agent - 浏览器登录类型
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置浏览器登录类型
     *
     * @param userAgent 浏览器登录类型
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}