package org.zhuyuqinlan.lemall.business.admin.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "修改订单费用信息参数")
public class OmsMoneyInfoParamRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "运费金额")
    private BigDecimal freightAmount;
    @Schema(description = "管理员后台调整订单使用的折扣金额")
    private BigDecimal discountAmount;
    @Schema(description = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
}
