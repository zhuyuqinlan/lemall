package org.zhuyuqinlan.lemall.business.portal.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsProductCategoryDTO;

import java.util.List;

@Getter
@Setter
public class PmsProductCategoryNode extends PmsProductCategoryDTO {
    private List<PmsProductCategoryNode> children;
}
