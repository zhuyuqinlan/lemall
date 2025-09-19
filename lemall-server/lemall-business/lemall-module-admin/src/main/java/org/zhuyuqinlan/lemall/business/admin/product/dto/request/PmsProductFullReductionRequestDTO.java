package org.zhuyuqinlan.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "商品满减请求DTO")
public class PmsProductFullReductionRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "满减金额门槛")
    private BigDecimal fullPrice;

    @Schema(description = "减少金额")
    private BigDecimal reducePrice;
}
