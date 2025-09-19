package org.zhuyuqinlan.lemall.business.admin.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "订单设置响应DTO")
public class OmsOrderSettingRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "秒杀订单超时关闭时间(分)")
    private Integer flashOrderOvertime;

    @Schema(description = "正常订单超时时间(分)")
    private Integer normalOrderOvertime;

    @Schema(description = "发货后自动确认收货时间（天）")
    private Integer confirmOvertime;

    @Schema(description = "自动完成交易时间，不能申请售后（天）")
    private Integer finishOvertime;

    @Schema(description = "订单完成后自动好评时间（天）")
    private Integer commentOvertime;
}
