package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "商品分类对应属性信息")
public class ProductAttrInfo {
    @Schema(description = "商品属性ID")
    private Long attributeId;
    @Schema(description = "商品属性分类ID")
    private Long attributeCategoryId;
}
