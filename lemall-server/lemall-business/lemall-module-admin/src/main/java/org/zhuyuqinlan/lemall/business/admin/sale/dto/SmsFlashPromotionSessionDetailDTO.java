package org.zhuyuqinlan.lemall.business.admin.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "包含商品数量的场次信息")
public class SmsFlashPromotionSessionDetailDTO extends SmsFlashPromotionSessionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品数量")
    private Long productCount;
}
