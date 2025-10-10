package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberCouponService;

import java.util.List;

@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    @Override
    public void add(Long couponId) {

    }

    @Override
    public List<SmsCouponHistoryResponseDTO> listHistory(Integer useStatus) {
        return List.of();
    }

    @Override
    public List<SmsCouponResponseDTO> list(Integer useStatus) {
        return List.of();
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartPromotionItems, Integer type) {
        return List.of();
    }

    @Override
    public List<SmsCouponResponseDTO> listByProduct(Long productId) {
        return List.of();
    }
}
