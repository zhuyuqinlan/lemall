package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 订单设置表
    */
@TableName(value = "oms_order_setting")
public class OmsOrderSetting {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 秒杀订单超时关闭时间(分)
     */
    @TableField(value = "flash_order_overtime")
    private Integer flashOrderOvertime;

    /**
     * 正常订单超时时间(分)
     */
    @TableField(value = "normal_order_overtime")
    private Integer normalOrderOvertime;

    /**
     * 发货后自动确认收货时间（天）
     */
    @TableField(value = "confirm_overtime")
    private Integer confirmOvertime;

    /**
     * 自动完成交易时间，不能申请售后（天）
     */
    @TableField(value = "finish_overtime")
    private Integer finishOvertime;

    /**
     * 订单完成后自动好评时间（天）
     */
    @TableField(value = "comment_overtime")
    private Integer commentOvertime;

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
     * 获取秒杀订单超时关闭时间(分)
     *
     * @return flash_order_overtime - 秒杀订单超时关闭时间(分)
     */
    public Integer getFlashOrderOvertime() {
        return flashOrderOvertime;
    }

    /**
     * 设置秒杀订单超时关闭时间(分)
     *
     * @param flashOrderOvertime 秒杀订单超时关闭时间(分)
     */
    public void setFlashOrderOvertime(Integer flashOrderOvertime) {
        this.flashOrderOvertime = flashOrderOvertime;
    }

    /**
     * 获取正常订单超时时间(分)
     *
     * @return normal_order_overtime - 正常订单超时时间(分)
     */
    public Integer getNormalOrderOvertime() {
        return normalOrderOvertime;
    }

    /**
     * 设置正常订单超时时间(分)
     *
     * @param normalOrderOvertime 正常订单超时时间(分)
     */
    public void setNormalOrderOvertime(Integer normalOrderOvertime) {
        this.normalOrderOvertime = normalOrderOvertime;
    }

    /**
     * 获取发货后自动确认收货时间（天）
     *
     * @return confirm_overtime - 发货后自动确认收货时间（天）
     */
    public Integer getConfirmOvertime() {
        return confirmOvertime;
    }

    /**
     * 设置发货后自动确认收货时间（天）
     *
     * @param confirmOvertime 发货后自动确认收货时间（天）
     */
    public void setConfirmOvertime(Integer confirmOvertime) {
        this.confirmOvertime = confirmOvertime;
    }

    /**
     * 获取自动完成交易时间，不能申请售后（天）
     *
     * @return finish_overtime - 自动完成交易时间，不能申请售后（天）
     */
    public Integer getFinishOvertime() {
        return finishOvertime;
    }

    /**
     * 设置自动完成交易时间，不能申请售后（天）
     *
     * @param finishOvertime 自动完成交易时间，不能申请售后（天）
     */
    public void setFinishOvertime(Integer finishOvertime) {
        this.finishOvertime = finishOvertime;
    }

    /**
     * 获取订单完成后自动好评时间（天）
     *
     * @return comment_overtime - 订单完成后自动好评时间（天）
     */
    public Integer getCommentOvertime() {
        return commentOvertime;
    }

    /**
     * 设置订单完成后自动好评时间（天）
     *
     * @param commentOvertime 订单完成后自动好评时间（天）
     */
    public void setCommentOvertime(Integer commentOvertime) {
        this.commentOvertime = commentOvertime;
    }
}