package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponHistoryMapper;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponMapper;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberCouponService;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    private final UmsMemberService memberService;
    private final SmsCouponMapper couponMapperPortal;
    private final SmsCouponHistoryMapper couponHistoryMapperPortal;

    @Override
    public void add(Long couponId) {

        //获取优惠券信息，判断数量

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
