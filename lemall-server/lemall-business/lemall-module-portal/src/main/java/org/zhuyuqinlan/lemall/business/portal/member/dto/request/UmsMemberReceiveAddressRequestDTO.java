package org.zhuyuqinlan.lemall.business.portal.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
@Getter
@Setter
@Schema(description = "会员收货地址请求DTO")
public class UmsMemberReceiveAddressRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "收货人名称")
    private String name;

    @Schema(description = "收货人电话")
    private String phoneNumber;

    @Schema(description = "是否为默认地址")
    private Integer defaultStatus;

    @Schema(description = "邮政编码")
    private String postCode;

    @Schema(description = "省份/直辖市")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区")
    private String region;

    @Schema(description = "详细地址(街道)")
    private String detailAddress;
}
