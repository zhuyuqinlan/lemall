package org.zhuyuqinlan.lemall.business.portal.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "SKU库存响应DTO")
public class PmsSkuStockResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "预警库存")
    private Integer lowStock;

    @Schema(description = "展示图片")
    private String pic;

    @Schema(description = "销量")
    private Integer sale;

    @Schema(description = "单品促销价格")
    private BigDecimal promotionPrice;

    @Schema(description = "锁定库存")
    private Integer lockStock;

    @Schema(description = "商品销售属性（JSON格式）")
    private String spData;
}

