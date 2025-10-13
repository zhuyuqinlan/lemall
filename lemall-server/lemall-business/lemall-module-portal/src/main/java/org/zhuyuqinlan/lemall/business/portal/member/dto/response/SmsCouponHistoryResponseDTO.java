package org.zhuyuqinlan.lemall.business.portal.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(description = "优惠券使用/领取历史响应DTO")
public class SmsCouponHistoryResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "优惠券编码")
    private String couponCode;

    @Schema(description = "领取人昵称")
    private String memberNickname;

    @Schema(description = "获取类型：0->后台赠送；1->主动获取")
    private Integer getType;

    @Schema(description = "领取时间")
    private Date createTime;

    @Schema(description = "使用状态：0->未使用；1->已使用；2->已过期")
    private Integer useStatus;

    @Schema(description = "使用时间")
    private Date useTime;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderSn;
}

