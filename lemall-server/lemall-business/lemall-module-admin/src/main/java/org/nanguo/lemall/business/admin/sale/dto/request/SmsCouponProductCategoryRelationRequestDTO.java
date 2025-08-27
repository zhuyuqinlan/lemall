package org.nanguo.lemall.business.admin.sale.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "优惠券和产品分类关系请求DTO")
public class SmsCouponProductCategoryRelationRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "产品分类ID")
    private Long productCategoryId;

    @Schema(description = "产品分类名称")
    private String productCategoryName;

    @Schema(description = "父分类名称")
    private String parentCategoryName;
}

