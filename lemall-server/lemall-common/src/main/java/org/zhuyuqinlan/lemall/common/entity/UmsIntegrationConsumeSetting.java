package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 积分消费设置
    */
@TableName(value = "ums_integration_consume_setting")
public class UmsIntegrationConsumeSetting {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 每一元需要抵扣的积分数量
     */
    @TableField(value = "deduction_per_amount")
    private Integer deductionPerAmount;

    /**
     * 每笔订单最高抵用百分比
     */
    @TableField(value = "max_percent_per_order")
    private Integer maxPercentPerOrder;

    /**
     * 每次使用积分最小单位100
     */
    @TableField(value = "use_unit")
    private Integer useUnit;

    /**
     * 是否可以和优惠券同用；0->不可以；1->可以
     */
    @TableField(value = "coupon_status")
    private Integer couponStatus;

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
     * 获取每一元需要抵扣的积分数量
     *
     * @return deduction_per_amount - 每一元需要抵扣的积分数量
     */
    public Integer getDeductionPerAmount() {
        return deductionPerAmount;
    }

    /**
     * 设置每一元需要抵扣的积分数量
     *
     * @param deductionPerAmount 每一元需要抵扣的积分数量
     */
    public void setDeductionPerAmount(Integer deductionPerAmount) {
        this.deductionPerAmount = deductionPerAmount;
    }

    /**
     * 获取每笔订单最高抵用百分比
     *
     * @return max_percent_per_order - 每笔订单最高抵用百分比
     */
    public Integer getMaxPercentPerOrder() {
        return maxPercentPerOrder;
    }

    /**
     * 设置每笔订单最高抵用百分比
     *
     * @param maxPercentPerOrder 每笔订单最高抵用百分比
     */
    public void setMaxPercentPerOrder(Integer maxPercentPerOrder) {
        this.maxPercentPerOrder = maxPercentPerOrder;
    }

    /**
     * 获取每次使用积分最小单位100
     *
     * @return use_unit - 每次使用积分最小单位100
     */
    public Integer getUseUnit() {
        return useUnit;
    }

    /**
     * 设置每次使用积分最小单位100
     *
     * @param useUnit 每次使用积分最小单位100
     */
    public void setUseUnit(Integer useUnit) {
        this.useUnit = useUnit;
    }

    /**
     * 获取是否可以和优惠券同用；0->不可以；1->可以
     *
     * @return coupon_status - 是否可以和优惠券同用；0->不可以；1->可以
     */
    public Integer getCouponStatus() {
        return couponStatus;
    }

    /**
     * 设置是否可以和优惠券同用；0->不可以；1->可以
     *
     * @param couponStatus 是否可以和优惠券同用；0->不可以；1->可以
     */
    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }
}