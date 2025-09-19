package org.zhuyuqinlan.lemall.business.admin.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Schema(name = "产品分类树节点")
public class PmsProductCategoryWithChildrenItem extends PmsProductCategoryResponseDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "子级分类")
    private List<PmsProductCategoryResponseDTO> children;
}
