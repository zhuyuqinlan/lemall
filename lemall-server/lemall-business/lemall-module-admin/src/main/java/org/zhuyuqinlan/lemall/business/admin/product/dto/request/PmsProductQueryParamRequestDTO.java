package org.zhuyuqinlan.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "产品查询参数")
@Getter
@Setter
public class PmsProductQueryParamRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "上架状态")
    private Integer publishStatus;

    @Schema(description = "审核状态")
    private Integer verifyStatus;

    @Schema(description = "商品名称模糊关键字")
    private String keyword;

    @Schema(description = "商品货号")
    private String productSn;

    @Schema(description = "商品分类编号")
    private Long productCategoryId;

    @Schema(description = "商品品牌编号")
    private Long brandId;
}
