package org.zhuyuqinlan.lemall.business.portal.member.service;

import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;

import java.util.List;

public interface UmsMemberCouponService {
    /**
     * 会员添加优惠券
     */
    void add(Long couponId);

    /**
     * 获取优惠券历史列表
     */
    List<SmsCouponHistoryResponseDTO> listHistory(Integer useStatus);


    /**
     * 获取用户优惠券列表
     */
    List<SmsCouponResponseDTO> list(Integer useStatus);

    /**
     * 根据购物车信息获取可用优惠券
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartPromotionItems, Integer type);

    /**
     * 获取当前商品相关优惠券
     */
    List<SmsCouponResponseDTO> listByProduct(Long productId);
}
