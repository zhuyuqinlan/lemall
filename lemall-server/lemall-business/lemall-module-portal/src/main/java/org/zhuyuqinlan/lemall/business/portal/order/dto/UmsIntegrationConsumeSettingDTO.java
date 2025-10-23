package org.zhuyuqinlan.lemall.business.portal.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "积分消费设置")
public class UmsIntegrationConsumeSettingDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "每一元需要抵扣的积分数量")
    private Integer deductionPerAmount;

    @Schema(description = "每笔订单最高抵用百分比")
    private Integer maxPercentPerOrder;

    @Schema(description = "每次使用积分最小单位，例如100")
    private Integer useUnit;

    @Schema(description = "是否可以和优惠券同用；0->不可以；1->可以")
    private Integer couponStatus;
}
