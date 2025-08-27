package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
    * 限时购表
    */
@TableName(value = "sms_flash_promotion")
public class SmsFlashPromotion {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 秒杀时间段名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 开始日期
     */
    @TableField(value = "start_date")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField(value = "end_date")
    private Date endDate;

    /**
     * 上下线状态
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

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
     * 获取秒杀时间段名称
     *
     * @return title - 秒杀时间段名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置秒杀时间段名称
     *
     * @param title 秒杀时间段名称
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取开始日期
     *
     * @return start_date - 开始日期
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始日期
     *
     * @param startDate 开始日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束日期
     *
     * @return end_date - 结束日期
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束日期
     *
     * @param endDate 结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取上下线状态
     *
     * @return status - 上下线状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置上下线状态
     *
     * @param status 上下线状态
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