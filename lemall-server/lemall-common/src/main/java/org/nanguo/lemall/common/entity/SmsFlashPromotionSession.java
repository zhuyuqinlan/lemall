package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 限时购场次表
    */
@TableName(value = "sms_flash_promotion_session")
public class SmsFlashPromotionSession {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 场次名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 每日开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 每日结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 启用状态：0->不启用；1->启用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 获取编号
     *
     * @return id - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取场次名称
     *
     * @return name - 场次名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置场次名称
     *
     * @param name 场次名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取每日开始时间
     *
     * @return start_time - 每日开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置每日开始时间
     *
     * @param startTime 每日开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取每日结束时间
     *
     * @return end_time - 每日结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置每日结束时间
     *
     * @param endTime 每日结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取启用状态：0->不启用；1->启用
     *
     * @return status - 启用状态：0->不启用；1->启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置启用状态：0->不启用；1->启用
     *
     * @param status 启用状态：0->不启用；1->启用
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
}