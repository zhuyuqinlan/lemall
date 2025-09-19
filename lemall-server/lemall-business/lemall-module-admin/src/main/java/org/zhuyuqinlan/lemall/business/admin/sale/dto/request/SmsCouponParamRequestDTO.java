package org.zhuyuqinlan.lemall.business.admin.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(name = "优惠券信息封装，包括绑定商品和绑定分类请求DTO")
public class SmsCouponParamRequestDTO extends SmsCouponRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "优惠券绑定的商品")
    private List<SmsCouponProductRelationRequestDTO> productRelationList;

    @Schema(title = "优惠券绑定的商品分类")
    private List<SmsCouponProductCategoryRelationRequestDTO> productCategoryRelationList;
}
