package org.zhuyuqinlan.lemall.business.admin.sale.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "首页人气推荐商品响应DTO")
public class SmsHomeRecommendProductResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "推荐状态：0->不推荐；1->推荐")
    private Integer recommendStatus;

    @Schema(description = "排序")
    private Integer sort;
}

