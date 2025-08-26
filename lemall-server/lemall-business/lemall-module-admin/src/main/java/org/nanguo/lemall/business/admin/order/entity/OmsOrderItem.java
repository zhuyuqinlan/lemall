package org.nanguo.lemall.business.admin.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

/**
    * 订单中所包含的商品
    */
@TableName(value = "oms_order_item")
public class OmsOrderItem {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单编号
     */
    @TableField(value = "order_sn")
    private String orderSn;

    @TableField(value = "product_id")
    private Long productId;

    @TableField(value = "product_pic")
    private String productPic;

    @TableField(value = "product_name")
    private String productName;

    @TableField(value = "product_brand")
    private String productBrand;

    @TableField(value = "product_sn")
    private String productSn;

    /**
     * 销售价格
     */
    @TableField(value = "product_price")
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    @TableField(value = "product_quantity")
    private Integer productQuantity;

    /**
     * 商品sku编号
     */
    @TableField(value = "product_sku_id")
    private Long productSkuId;

    /**
     * 商品sku条码
     */
    @TableField(value = "product_sku_code")
    private String productSkuCode;

    /**
     * 商品分类id
     */
    @TableField(value = "product_category_id")
    private Long productCategoryId;

    /**
     * 商品促销名称
     */
    @TableField(value = "promotion_name")
    private String promotionName;

    /**
     * 商品促销分解金额
     */
    @TableField(value = "promotion_amount")
    private BigDecimal promotionAmount;

    /**
     * 优惠券优惠分解金额
     */
    @TableField(value = "coupon_amount")
    private BigDecimal couponAmount;

    /**
     * 积分优惠分解金额
     */
    @TableField(value = "integration_amount")
    private BigDecimal integrationAmount;

    /**
     * 该商品经过优惠后的分解金额
     */
    @TableField(value = "real_amount")
    private BigDecimal realAmount;

    @TableField(value = "gift_integration")
    private Integer giftIntegration;

    @TableField(value = "gift_growth")
    private Integer giftGrowth;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    @TableField(value = "product_attr")
    private String productAttr;

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
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单编号
     *
     * @return order_sn - 订单编号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单编号
     *
     * @param orderSn 订单编号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
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
     * @return product_pic
     */
    public String getProductPic() {
        return productPic;
    }

    /**
     * @param productPic
     */
    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    /**
     * @return product_name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return product_brand
     */
    public String getProductBrand() {
        return productBrand;
    }

    /**
     * @param productBrand
     */
    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    /**
     * @return product_sn
     */
    public String getProductSn() {
        return productSn;
    }

    /**
     * @param productSn
     */
    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    /**
     * 获取销售价格
     *
     * @return product_price - 销售价格
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    /**
     * 设置销售价格
     *
     * @param productPrice 销售价格
     */
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * 获取购买数量
     *
     * @return product_quantity - 购买数量
     */
    public Integer getProductQuantity() {
        return productQuantity;
    }

    /**
     * 设置购买数量
     *
     * @param productQuantity 购买数量
     */
    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     * 获取商品sku编号
     *
     * @return product_sku_id - 商品sku编号
     */
    public Long getProductSkuId() {
        return productSkuId;
    }

    /**
     * 设置商品sku编号
     *
     * @param productSkuId 商品sku编号
     */
    public void setProductSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
    }

    /**
     * 获取商品sku条码
     *
     * @return product_sku_code - 商品sku条码
     */
    public String getProductSkuCode() {
        return productSkuCode;
    }

    /**
     * 设置商品sku条码
     *
     * @param productSkuCode 商品sku条码
     */
    public void setProductSkuCode(String productSkuCode) {
        this.productSkuCode = productSkuCode;
    }

    /**
     * 获取商品分类id
     *
     * @return product_category_id - 商品分类id
     */
    public Long getProductCategoryId() {
        return productCategoryId;
    }

    /**
     * 设置商品分类id
     *
     * @param productCategoryId 商品分类id
     */
    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    /**
     * 获取商品促销名称
     *
     * @return promotion_name - 商品促销名称
     */
    public String getPromotionName() {
        return promotionName;
    }

    /**
     * 设置商品促销名称
     *
     * @param promotionName 商品促销名称
     */
    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    /**
     * 获取商品促销分解金额
     *
     * @return promotion_amount - 商品促销分解金额
     */
    public BigDecimal getPromotionAmount() {
        return promotionAmount;
    }

    /**
     * 设置商品促销分解金额
     *
     * @param promotionAmount 商品促销分解金额
     */
    public void setPromotionAmount(BigDecimal promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    /**
     * 获取优惠券优惠分解金额
     *
     * @return coupon_amount - 优惠券优惠分解金额
     */
    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    /**
     * 设置优惠券优惠分解金额
     *
     * @param couponAmount 优惠券优惠分解金额
     */
    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    /**
     * 获取积分优惠分解金额
     *
     * @return integration_amount - 积分优惠分解金额
     */
    public BigDecimal getIntegrationAmount() {
        return integrationAmount;
    }

    /**
     * 设置积分优惠分解金额
     *
     * @param integrationAmount 积分优惠分解金额
     */
    public void setIntegrationAmount(BigDecimal integrationAmount) {
        this.integrationAmount = integrationAmount;
    }

    /**
     * 获取该商品经过优惠后的分解金额
     *
     * @return real_amount - 该商品经过优惠后的分解金额
     */
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    /**
     * 设置该商品经过优惠后的分解金额
     *
     * @param realAmount 该商品经过优惠后的分解金额
     */
    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    /**
     * @return gift_integration
     */
    public Integer getGiftIntegration() {
        return giftIntegration;
    }

    /**
     * @param giftIntegration
     */
    public void setGiftIntegration(Integer giftIntegration) {
        this.giftIntegration = giftIntegration;
    }

    /**
     * @return gift_growth
     */
    public Integer getGiftGrowth() {
        return giftGrowth;
    }

    /**
     * @param giftGrowth
     */
    public void setGiftGrowth(Integer giftGrowth) {
        this.giftGrowth = giftGrowth;
    }

    /**
     * 获取商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     *
     * @return product_attr - 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    public String getProductAttr() {
        return productAttr;
    }

    /**
     * 设置商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     *
     * @param productAttr 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }
}