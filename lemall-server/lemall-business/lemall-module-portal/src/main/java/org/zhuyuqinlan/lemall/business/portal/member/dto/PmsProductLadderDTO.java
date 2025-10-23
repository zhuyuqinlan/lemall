package org.zhuyuqinlan.lemall.business.portal.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "产品阶梯价格响应DTO")
public class PmsProductLadderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "满足的商品数量")
    private Integer count;

    @Schema(description = "折扣")
    private BigDecimal discount;

    @Schema(description = "折后价格")
    private BigDecimal price;
}

