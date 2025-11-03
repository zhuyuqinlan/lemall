package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "包含有分类下属性的dto")
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategoryDTO {

    @Schema(description = "商品属性列表")
    private List<PmsProductAttributeDTO> productAttributeList;
}