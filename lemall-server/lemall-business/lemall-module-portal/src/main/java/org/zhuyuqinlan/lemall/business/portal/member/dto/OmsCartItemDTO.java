package org.zhuyuqinlan.lemall.business.portal.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "购物车项响应DTO")
public class OmsCartItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品SKU ID")
    private Long productSkuId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "添加到购物车的价格")
    private BigDecimal price;

    @Schema(description = "商品主图")
    private String productPic;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品副标题（卖点）")
    private String productSubTitle;

    @Schema(description = "商品SKU条码")
    private String productSkuCode;

    @Schema(description = "会员昵称")
    private String memberNickname;

    @Schema(description = "创建时间")
    private LocalDateTime createDate;

    @Schema(description = "修改时间")
    private LocalDateTime modifyDate;

    @Schema(description = "是否删除")
    private Integer deleteStatus;

    @Schema(description = "商品分类ID")
    private Long productCategoryId;

    @Schema(description = "商品品牌")
    private String productBrand;

    @Schema(description = "商品编号")
    private String productSn;

    @Schema(description = "商品销售属性")
    private String productAttr;
}

