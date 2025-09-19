package org.zhuyuqinlan.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.admin.content.dto.request.CmsPrefrenceAreaProductRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.content.dto.request.CmsSubjectProductRelationRequestDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(name = "创建和修改商品时使用的参数")
public class PmsProductParamRequestDTO extends PmsProductRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品阶梯价格设置")
    private List<PmsProductLadderRequestDTO> productLadderList;

    @Schema(description = "商品满减价格设置")
    private List<PmsProductFullReductionRequestDTO> productFullReductionList;

    @Schema(description = "商品会员价格设置")
    private List<PmsMemberPriceRequestDTO> memberPriceList;

    @Schema(description = "商品的sku库存信息")
    private List<PmsSkuStockRequestDTO> skuStockList;

    @Schema(description = "商品参数及自定义规格属性")
    private List<PmsProductAttributeValueRequestDTO> productAttributeValueList;

    @Schema(description = "专题和商品关系")
    private List<CmsSubjectProductRelationRequestDTO> subjectProductRelationList;

    @Schema(description = "优选专区和商品的关系")
    private List<CmsPrefrenceAreaProductRelationRequestDTO> prefrenceAreaProductRelationList;
}
