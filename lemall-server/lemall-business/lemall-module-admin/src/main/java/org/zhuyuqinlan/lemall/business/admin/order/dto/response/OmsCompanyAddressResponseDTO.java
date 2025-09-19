package org.zhuyuqinlan.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "公司收发货地址响应DTO")
public class OmsCompanyAddressResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "地址名称")
    private String addressName;

    @Schema(description = "是否默认发货地址：0->否；1->是")
    private Integer sendStatus;

    @Schema(description = "是否默认收货地址：0->否；1->是")
    private Integer receiveStatus;

    @Schema(description = "收/发货人姓名")
    private String name;

    @Schema(description = "收货人电话")
    private String phone;

    @Schema(description = "省/直辖市")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区")
    private String region;

    @Schema(description = "详细地址")
    private String detailAddress;
}

