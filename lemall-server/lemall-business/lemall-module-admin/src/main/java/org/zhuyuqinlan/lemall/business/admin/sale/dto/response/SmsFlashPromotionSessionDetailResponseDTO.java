package org.zhuyuqinlan.lemall.business.admin.sale.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "包含商品数量的场次信息")
public class SmsFlashPromotionSessionDetailResponseDTO extends SmsFlashPromotionSessionResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品数量")
    private Long productCount;
}
