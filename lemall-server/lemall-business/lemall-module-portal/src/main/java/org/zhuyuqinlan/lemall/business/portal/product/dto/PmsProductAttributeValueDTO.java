package org.zhuyuqinlan.lemall.business.portal.product.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "商品属性值响应DTO")
public class PmsProductAttributeValueDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "商品属性id")
    private Long productAttributeId;

    @Schema(description = "属性值（参数单值，规格多值时用逗号分隔）")
    private String value;
}

