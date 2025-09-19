package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 优惠券和产品的关系表
    */
@TableName(value = "sms_coupon_product_relation")
public class SmsCouponProductRelation {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "coupon_id")
    private Long couponId;

    @TableField(value = "product_id")
    private Long productId;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 商品编码
     */
    @TableField(value = "product_sn")
    private String productSn;

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
     * @return product_id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取商品名称
     *
     * @return product_name - 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称
     *
     * @param productName 商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取商品编码
     *
     * @return product_sn - 商品编码
     */
    public String getProductSn() {
        return productSn;
    }

    /**
     * 设置商品编码
     *
     * @param productSn 商品编码
     */
    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }
}