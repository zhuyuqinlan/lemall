package org.zhuyuqinlan.lemall.business.admin.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductDTO;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "限时购及商品信息封装")
public class SmsFlashPromotionProductResponseDTO extends SmsFlashPromotionProductRelationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "关联商品")
    private PmsProductDTO product;
}
