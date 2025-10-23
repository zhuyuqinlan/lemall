package org.zhuyuqinlan.lemall.business.portal.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PromotionProduct extends PmsProductDTO {
    //商品库存信息
    private List<PmsSkuStockDTO> skuStockList;
    //商品打折信息
    private List<PmsProductLadderDTO> productLadderList;
    //商品满减信息
    private List<PmsProductFullReductionDTO> productFullReductionList;
}
