package org.zhuyuqinlan.lemall.business.portal.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsProductFullReductionResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsProductLadderResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsProductResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.PmsSkuStockResponseDTO;

import java.util.List;

@Getter
@Setter
public class PromotionProduct extends PmsProductResponseDTO {
    //商品库存信息
    private List<PmsSkuStockResponseDTO> skuStockList;
    //商品打折信息
    private List<PmsProductLadderResponseDTO> productLadderList;
    //商品满减信息
    private List<PmsProductFullReductionResponseDTO> productFullReductionList;
}
