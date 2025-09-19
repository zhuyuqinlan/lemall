package org.zhuyuqinlan.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "商品会员价格请求DTO")
public class PmsMemberPriceRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "会员等级id")
    private Long memberLevelId;

    @Schema(description = "会员价格")
    private BigDecimal memberPrice;

    @Schema(description = "会员等级名称")
    private String memberLevelName;
}
