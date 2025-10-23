package org.zhuyuqinlan.lemall.business.portal.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartProduct extends PmsProductDTO {
    private List<PmsProductAttributeDTO> productAttributeList;
    private List<PmsSkuStockDTO> skuStockList;
}
