package org.zhuyuqinlan.lemall.business.admin.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "限时购商品关系响应DTO")
public class SmsFlashPromotionProductRelationRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "限时购ID")
    private Long flashPromotionId;

    @Schema(description = "限时购场次ID")
    private Long flashPromotionSessionId;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "限时购价格")
    private BigDecimal flashPromotionPrice;

    @Schema(description = "限时购数量")
    private Integer flashPromotionCount;

    @Schema(description = "每人限购数量")
    private Integer flashPromotionLimit;

    @Schema(description = "排序")
    private Integer sort;
}

