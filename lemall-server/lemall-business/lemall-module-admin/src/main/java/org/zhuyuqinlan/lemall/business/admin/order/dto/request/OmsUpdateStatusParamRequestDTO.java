package org.zhuyuqinlan.lemall.business.admin.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "确认收货提交参数")
public class OmsUpdateStatusParamRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "服务单号")
    private Long id;
    @Schema(description = "收货地址关联id")
    private Long companyAddressId;
    @Schema(description = "确认退款金额")
    private BigDecimal returnAmount;
    @Schema(description = "处理备注")
    private String handleNote;
    @Schema(description = "处理人")
    private String handleMan;
    @Schema(description = "收货备注")
    private String receiveNote;
    @Schema(description = "收货人")
    private String receiveMan;
    @Schema(description = "申请状态：1->退货中；2->已完成；3->已拒绝")
    private Integer status;
}
