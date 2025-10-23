package org.zhuyuqinlan.lemall.business.portal.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsBrandDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.*;

import java.util.List;

@Getter
@Setter
public class PmsPortalProductDetail{
    @Schema(description = "商品信息")
    private PmsProductDTO product;
    @Schema(description = "商品品牌")
    private PmsBrandDTO brand;
    @Schema(description = "商品属性与参数")
    private List<PmsProductAttributeDTO> productAttributeList;
    @Schema(description = "手动录入的商品属性与参数值")
    private List<PmsProductAttributeValueDTO> productAttributeValueList;
    @Schema(description = "商品的sku库存信息")
    private List<PmsSkuStockDTO> skuStockList;
    @Schema(description = "商品阶梯价格设置")
    private List<PmsProductLadderDTO> productLadderList;
    @Schema(description = "商品满减价格设置")
    private List<PmsProductFullReductionDTO> productFullReductionList;
    @Schema(description = "商品可用优惠券")
    private List<SmsCouponDTO> couponList;
}
