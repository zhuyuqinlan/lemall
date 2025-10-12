package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 优惠券使用、领取历史表
    */
@TableName(value = "sms_coupon_history")
public class SmsCouponHistory {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "coupon_id")
    private Long couponId;

    @TableField(value = "member_id")
    private Long memberId;

    @TableField(value = "coupon_code")
    private String couponCode;

    /**
     * 领取人昵称
     */
    @TableField(value = "member_nickname")
    private String memberNickname;

    /**
     * 获取类型：0->后台赠送；1->主动获取
     */
    @TableField(value = "get_type")
    private Integer getType;

    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 使用状态：0->未使用；1->已使用；2->已过期
     */
    @TableField(value = "use_status")
    private Integer useStatus;

    /**
     * 使用时间
     */
    @TableField(value = "use_time")
    private Date useTime;

    /**
     * 订单编号
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单号码
     */
    @TableField(value = "order_sn")
    private String orderSn;

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
     * @return coupon_id
     */
    public Long getCouponId() {
        return couponId;
    }

    /**
     * @param couponId
     */
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    /**
     * @return member_id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @param memberId
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return coupon_code
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     * @param couponCode
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    /**
     * 获取领取人昵称
     *
     * @return member_nickname - 领取人昵称
     */
    public String getMemberNickname() {
        return memberNickname;
    }

    /**
     * 设置领取人昵称
     *
     * @param memberNickname 领取人昵称
     */
    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    /**
     * 获取获取类型：0->后台赠送；1->主动获取
     *
     * @return get_type - 获取类型：0->后台赠送；1->主动获取
     */
    public Integer getGetType() {
        return getType;
    }

    /**
     * 设置获取类型：0->后台赠送；1->主动获取
     *
     * @param getType 获取类型：0->后台赠送；1->主动获取
     */
    public void setGetType(Integer getType) {
        this.getType = getType;
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
     * 获取使用状态：0->未使用；1->已使用；2->已过期
     *
     * @return use_status - 使用状态：0->未使用；1->已使用；2->已过期
     */
    public Integer getUseStatus() {
        return useStatus;
    }

    /**
     * 设置使用状态：0->未使用；1->已使用；2->已过期
     *
     * @param useStatus 使用状态：0->未使用；1->已使用；2->已过期
     */
    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * 获取使用时间
     *
     * @return use_time - 使用时间
     */
    public Date getUseTime() {
        return useTime;
    }

    /**
     * 设置使用时间
     *
     * @param useTime 使用时间
     */
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取订单编号
     *
     * @return order_id - 订单编号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单编号
     *
     * @param orderId 订单编号
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单号码
     *
     * @return order_sn - 订单号码
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单号码
     *
     * @param orderSn 订单号码
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}