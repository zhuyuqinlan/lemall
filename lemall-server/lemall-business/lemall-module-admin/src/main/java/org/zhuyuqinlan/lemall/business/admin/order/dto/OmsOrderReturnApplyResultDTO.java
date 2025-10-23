package org.zhuyuqinlan.lemall.business.admin.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "申请信息封装")
public class OmsOrderReturnApplyResultDTO extends OmsOrderReturnApplyDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "公司收货地址")
    private OmsCompanyAddressDTO companyAddress;
}
