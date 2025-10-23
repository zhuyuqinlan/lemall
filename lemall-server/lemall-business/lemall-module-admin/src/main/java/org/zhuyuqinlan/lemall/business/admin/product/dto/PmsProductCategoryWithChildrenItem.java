package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Schema(description = "产品分类树节点")
public class PmsProductCategoryWithChildrenItem extends PmsProductCategoryDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "子级分类")
    private List<PmsProductCategoryDTO> children;
}
