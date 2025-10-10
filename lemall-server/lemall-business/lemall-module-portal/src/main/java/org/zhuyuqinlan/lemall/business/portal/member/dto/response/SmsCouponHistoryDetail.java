package org.zhuyuqinlan.lemall.business.portal.member.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SmsCouponHistoryDetail extends SmsCouponHistoryResponseDTO{
    //相关优惠券信息
    private SmsCouponResponseDTO coupon;
    //优惠券关联商品
    private List<SmsCouponProductRelationResponseDTO> productRelationList;
    //优惠券关联商品分类
    private List<SmsCouponProductCategoryRelationResponseDTO> categoryRelationList;

}
