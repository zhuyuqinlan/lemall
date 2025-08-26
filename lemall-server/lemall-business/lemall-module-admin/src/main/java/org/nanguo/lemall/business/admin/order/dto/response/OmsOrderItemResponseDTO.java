package org.nanguo.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "订单商品项响应DTO")
public class OmsOrderItemResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

    @Schema(description = "商品货号")
    private String productSn;

    @Schema(description = "销售价格")
    private BigDecimal productPrice;

    @Schema(description = "购买数量")
    private Integer productQuantity;

    @Schema(description = "商品SKU ID")
    private Long productSkuId;

    @Schema(description = "商品SKU编码")
    private String productSkuCode;

    @Schema(description = "商品分类ID")
    private Long productCategoryId;

    @Schema(description = "促销活动名称")
    private String promotionName;

    @Schema(description = "促销分摊金额")
    private BigDecimal promotionAmount;

    @Schema(description = "优惠券分摊金额")
    private BigDecimal couponAmount;

    @Schema(description = "积分分摊金额")
    private BigDecimal integrationAmount;

    @Schema(description = "实际金额（分摊后单品金额）")
    private BigDecimal realAmount;

    @Schema(description = "赠送积分")
    private Integer giftIntegration;

    @Schema(description = "赠送成长值")
    private Integer giftGrowth;

    @Schema(description = "商品销售属性(JSON字符串)")
    private String productAttr;
}
