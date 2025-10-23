package org.zhuyuqinlan.lemall.business.portal.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SmsCouponHistoryDetail extends SmsCouponHistoryDTO {
    //相关优惠券信息
    private SmsCouponDTO coupon;
    //优惠券关联商品
    private List<SmsCouponProductRelationDTO> productRelationList;
    //优惠券关联商品分类
    private List<SmsCouponProductCategoryRelationDTO> categoryRelationList;

}
