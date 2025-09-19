package org.zhuyuqinlan.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "申请信息封装")
public class OmsOrderReturnApplyResultResponseDTO extends OmsOrderReturnApplyResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "公司收货地址")
    private  OmsCompanyAddressResponseDTO companyAddress;
}
