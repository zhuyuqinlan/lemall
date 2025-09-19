package org.zhuyuqinlan.lemall.business.admin.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "订单发货参数")
public class OmsOrderDeliveryParamRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "物流公司")
    private String deliveryCompany;
    @Schema(description = "物流单号")
    private String deliverySn;
}
