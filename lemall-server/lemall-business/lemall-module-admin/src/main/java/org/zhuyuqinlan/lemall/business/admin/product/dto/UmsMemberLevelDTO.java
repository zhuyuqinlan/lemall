package org.zhuyuqinlan.lemall.business.admin.product.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "会员等级表")
public class UmsMemberLevelDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "等级名称，最长 100 字符")
    private String name;

    @Schema(description = "成长值")
    private Integer growthPoint;

    @Schema(description = "是否为默认等级：0->不是；1->是")
    private Integer defaultStatus;

    @Schema(description = "免运费标准，保留两位小数")
    private BigDecimal freeFreightPoint;

    @Schema(description = "每次评价获取的成长值")
    private Integer commentGrowthPoint;

    @Schema(description = "是否有免邮特权")
    private Integer priviledgeFreeFreight;

    @Schema(description = "是否有签到特权")
    private Integer priviledgeSignIn;

    @Schema(description = "是否有评论获奖励特权")
    private Integer priviledgeComment;

    @Schema(description = "是否有专享活动特权")
    private Integer priviledgePromotion;

    @Schema(description = "是否有会员价格特权")
    private Integer priviledgeMemberPrice;

    @Schema(description = "是否有生日特权")
    private Integer priviledgeBirthday;

    @Schema(description = "备注，最长 200 字符")
    private String note;
}
