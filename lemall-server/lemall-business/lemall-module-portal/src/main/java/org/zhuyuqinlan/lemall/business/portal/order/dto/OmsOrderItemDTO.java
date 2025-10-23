package org.zhuyuqinlan.lemall.business.portal.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "OmsOrderItemDTO", description = "订单中所包含的商品")
public class OmsOrderItemDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品图片")
    private String productPic;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品品牌")
    private String productBrand;

    @Schema(description = "商品编号")
    private String productSn;

    @Schema(description = "销售价格")
    private BigDecimal productPrice;

    @Schema(description = "购买数量")
    private Integer productQuantity;

    @Schema(description = "商品SKU编号")
    private Long productSkuId;

    @Schema(description = "商品SKU条码")
    private String productSkuCode;

    @Schema(description = "商品分类ID")
    private Long productCategoryId;

    @Schema(description = "商品促销名称")
    private String promotionName;

    @Schema(description = "商品促销分解金额")
    private BigDecimal promotionAmount;

    @Schema(description = "优惠券优惠分解金额")
    private BigDecimal couponAmount;

    @Schema(description = "积分优惠分解金额")
    private BigDecimal integrationAmount;

    @Schema(description = "该商品经过优惠后的分解金额")
    private BigDecimal realAmount;

    @Schema(description = "赠送的积分")
    private Integer giftIntegration;

    @Schema(description = "赠送的成长值")
    private Integer giftGrowth;

    @Schema(description = "商品销售属性(JSON): [{\"key\":\"颜色\",\"value\":\"红色\"},{\"key\":\"容量\",\"value\":\"4G\"}]")
    private String productAttr;
}
