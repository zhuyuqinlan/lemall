package org.nanguo.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(name = "订单详情信息")
public class OmsOrderDetailResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单商品列表")
    private List<OmsOrderItemResponseDTO> orderItemList;

    @Schema(description = "订单操作记录列表")
    private List<OmsOrderOperateHistoryResponseDTO> historyList;
}
