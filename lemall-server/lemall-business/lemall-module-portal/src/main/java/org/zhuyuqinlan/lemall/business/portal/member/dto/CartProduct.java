package org.zhuyuqinlan.lemall.business.portal.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsProductAttributeResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsProductResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsSkuStockResponseDTO;

import java.util.List;

@Getter
@Setter
public class CartProduct extends PmsProductResponseDTO {
    private List<PmsProductAttributeResponseDTO> productAttributeList;
    private List<PmsSkuStockResponseDTO> skuStockList;
}
