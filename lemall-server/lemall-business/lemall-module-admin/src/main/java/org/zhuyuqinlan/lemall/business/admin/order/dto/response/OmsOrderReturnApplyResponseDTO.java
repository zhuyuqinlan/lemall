package org.zhuyuqinlan.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "订单退货申请响应DTO")
public class OmsOrderReturnApplyResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "收货地址表ID")
    private Long companyAddressId;

    @Schema(description = "退货商品ID")
    private Long productId;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

    @Schema(description = "会员用户名")
    private String memberUsername;

    @Schema(description = "退款金额")
    private BigDecimal returnAmount;

    @Schema(description = "退货人姓名")
    private String returnName;

    @Schema(description = "退货人电话")
    private String returnPhone;

    @Schema(description = "申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "商品图片")
    private String productPic;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品品牌")
    private String productBrand;

    @Schema(description = "商品销售属性：颜色：红色；尺码：xl;")
    private String productAttr;

    @Schema(description = "退货数量")
    private Integer productCount;

    @Schema(description = "商品单价")
    private BigDecimal productPrice;

    @Schema(description = "商品实际支付单价")
    private BigDecimal productRealPrice;

    @Schema(description = "原因")
    private String reason;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "凭证图片，以逗号隔开")
    private String proofPics;

    @Schema(description = "处理备注")
    private String handleNote;

    @Schema(description = "处理人员")
    private String handleMan;

    @Schema(description = "收货人")
    private String receiveMan;

    @Schema(description = "收货时间")
    private LocalDateTime receiveTime;

    @Schema(description = "收货备注")
    private String receiveNote;
}

