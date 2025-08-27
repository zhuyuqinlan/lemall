package org.nanguo.lemall.business.admin.sale.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "优惠券响应DTO")
public class SmsCouponResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优惠券类型：0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券")
    private Integer type;

    @Schema(description = "优惠券名称")
    private String name;

    @Schema(description = "使用平台：0->全部；1->移动；2->PC")
    private Integer platform;

    @Schema(description = "数量")
    private Integer count;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "每人限领张数")
    private Integer perLimit;

    @Schema(description = "使用门槛；0表示无门槛")
    private BigDecimal minPoint;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "使用类型：0->全场通用；1->指定分类；2->指定商品")
    private Integer useType;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "发行数量")
    private Integer publishCount;

    @Schema(description = "已使用数量")
    private Integer useCount;

    @Schema(description = "领取数量")
    private Integer receiveCount;

    @Schema(description = "可以领取的日期")
    private LocalDateTime enableTime;

    @Schema(description = "优惠码")
    private String code;

    @Schema(description = "可领取的会员类型：0->无限时")
    private Integer memberLevel;
}

