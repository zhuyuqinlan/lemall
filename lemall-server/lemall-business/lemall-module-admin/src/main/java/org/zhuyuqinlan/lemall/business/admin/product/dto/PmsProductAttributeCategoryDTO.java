package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "产品属性分类DTO")
public class PmsProductAttributeCategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "属性数量")
    private Integer attributeCount;

    @Schema(description = "参数数量")
    private Integer paramCount;
}
