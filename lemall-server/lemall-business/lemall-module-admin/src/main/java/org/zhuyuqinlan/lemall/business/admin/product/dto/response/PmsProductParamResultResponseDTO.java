package org.zhuyuqinlan.lemall.business.admin.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "查询单个产品进行修改时返回的结果")
public class PmsProductParamResultResponseDTO extends PmsProductParamResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品所选分类的父id")
    private Long cateParentId;
}
