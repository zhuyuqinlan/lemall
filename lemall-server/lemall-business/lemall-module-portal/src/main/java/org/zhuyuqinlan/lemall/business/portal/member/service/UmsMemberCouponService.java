package org.zhuyuqinlan.lemall.business.portal.member.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.dao.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.*;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.mapper.*;
import org.zhuyuqinlan.lemall.common.response.BizException;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UmsMemberCouponService {

    private final UmsMemberService memberService;
    private final SmsCouponMapper couponMapperPortal;
    private final SmsCouponHistoryMapper couponHistoryMapperPortal;
    private final SmsCouponProductRelationMapper couponProductRelationMapper;
    private final SmsCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;
    private final PmsProductMapper productMapper;
    private final SmsCouponHistoryDao smsCouponHistoryDao;

    /**
     * 会员添加优惠券
     */
    @Transactional
    public void add(Long couponId) {
        SmsCoupon smsCoupon = couponMapperPortal.selectById(couponId);
        if (smsCoupon == null) throw new BizException("优惠劵不存在");
        if (smsCoupon.getCount() <= 0) throw new BizException("优惠劵已经领完了");

        Date now = new Date();
        if (now.before(smsCoupon.getEnableTime())) throw new BizException("优惠劵还没到领取时间");

        Long count = couponHistoryMapperPortal.selectCount(
                Wrappers.<SmsCouponHistory>lambdaQuery().eq(SmsCouponHistory::getCouponId, couponId)
        );
        if (count >= smsCoupon.getPerLimit()) throw new BizException("您已经领取过该优惠劵");

        UmsMemberResponseDTO currentMember = memberService.getCurrentMember();
        SmsCouponHistory smsCouponHistory = new SmsCouponHistory();
        smsCouponHistory.setCouponId(couponId);
        smsCouponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        smsCouponHistory.setCreateTime(now);
        smsCouponHistory.setMemberId(currentMember.getId());
        smsCouponHistory.setMemberNickname(currentMember.getNickname());
        smsCouponHistory.setGetType(1);
        smsCouponHistory.setUseStatus(0);
        couponHistoryMapperPortal.insert(smsCouponHistory);

        smsCoupon.setCount(smsCoupon.getCount() - 1);
        smsCoupon.setReceiveCount(smsCoupon.getReceiveCount() == null ? 1 : smsCoupon.getReceiveCount() + 1);
        couponMapperPortal.updateById(smsCoupon);
    }

    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        String timeStr = String.valueOf(System.currentTimeMillis());
        sb.append(timeStr.substring(timeStr.length() - 8));
        for (int i = 0; i < 4; i++) sb.append(new Random().nextInt(10));
        String memberStr = memberId.toString();
        sb.append(memberStr.length() <= 4 ? String.format("%04d", memberId) : memberStr.substring(memberStr.length() - 4));
        return sb.toString();
    }

    /**
     * 获取优惠券历史列表
     */
    public List<SmsCouponHistoryResponseDTO> listHistory(Integer useStatus) {
        List<SmsCouponHistory> histories = couponHistoryMapperPortal.selectList(
                Wrappers.<SmsCouponHistory>lambdaQuery()
                        .eq(SmsCouponHistory::getMemberId, StpMemberUtil.getLoginId())
                        .eq(useStatus != null, SmsCouponHistory::getUseStatus, useStatus)
        );
        List<SmsCouponHistoryResponseDTO> result = new ArrayList<>();
        BeanUtils.copyProperties(histories, result);
        return result;
    }

    /**
     * 获取用户优惠券列表
     */
    public List<SmsCouponResponseDTO> list(Integer useStatus) {
        return smsCouponHistoryDao.getCouponList(Long.parseLong(StpMemberUtil.getLoginId().toString()), useStatus);
    }

    /**
     * 根据购物车信息获取可用优惠券
     */
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartPromotionItems, Integer type) {
        Date now = new Date();
        List<SmsCouponHistoryDetail> allList = smsCouponHistoryDao.getDetailList(Long.parseLong(StpMemberUtil.getLoginId().toString()));
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();

        for (SmsCouponHistoryDetail coupon : allList) {
            Integer useType = coupon.getCoupon().getUseType();
            BigDecimal minPoint = coupon.getCoupon().getMinPoint();
            Date endTime = coupon.getCoupon().getEndTime();

            BigDecimal totalAmount = BigDecimal.ZERO;
            if (useType.equals(0)) totalAmount = calcTotalAmount(cartPromotionItems);
            else if (useType.equals(1)) {
                List<Long> categoryIds = coupon.getCategoryRelationList().stream().map(SmsCouponProductCategoryRelationResponseDTO::getProductCategoryId).toList();
                totalAmount = calcTotalAmountByproductCategoryId(cartPromotionItems, categoryIds);
            } else if (useType.equals(2)) {
                List<Long> productIds = coupon.getProductRelationList().stream().map(SmsCouponProductRelationResponseDTO::getProductId).toList();
                totalAmount = calcTotalAmountByProductId(cartPromotionItems, productIds);
            }

            if (now.before(endTime) && totalAmount.intValue() >= 0 && (useType.equals(0) || totalAmount.subtract(minPoint).intValue() >= 0)) {
                enableList.add(coupon);
            } else {
                disableList.add(coupon);
            }
        }

        return type.equals(1) ? enableList : disableList;
    }

    /**
     * 获取当前商品相关优惠券
     */
    public List<SmsCouponResponseDTO> listByProduct(Long productId) {
        List<Long> allCouponIds = new ArrayList<>();

        List<SmsCouponProductRelation> cprList = couponProductRelationMapper.selectList(
                Wrappers.<SmsCouponProductRelation>lambdaQuery().eq(SmsCouponProductRelation::getProductId, productId)
        );
        if (CollUtil.isNotEmpty(cprList)) allCouponIds.addAll(cprList.stream().map(SmsCouponProductRelation::getCouponId).toList());

        PmsProduct product = productMapper.selectById(productId);
        List<SmsCouponProductCategoryRelation> cpcrList = couponProductCategoryRelationMapper.selectList(
                Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery().eq(SmsCouponProductCategoryRelation::getProductCategoryId, product.getProductCategoryId())
        );
        if (CollUtil.isNotEmpty(cpcrList)) allCouponIds.addAll(cpcrList.stream().map(SmsCouponProductCategoryRelation::getCouponId).toList());

        if (CollUtil.isEmpty(allCouponIds)) return new ArrayList<>();

        Date now = new Date();
        return couponMapperPortal.selectList(
                Wrappers.<SmsCoupon>lambdaQuery()
                        .and(w -> w.lt(SmsCoupon::getEndTime, now).gt(SmsCoupon::getStartTime, now).eq(SmsCoupon::getUseType, 0))
                        .or(w -> w.lt(SmsCoupon::getEndTime, now).gt(SmsCoupon::getStartTime, now).ne(SmsCoupon::getUseType, 0).in(SmsCoupon::getId, allCouponIds))
        ).stream().map(e -> {
            SmsCouponResponseDTO dto = new SmsCouponResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartPromotionItem item : cartItemList) total = total.add(item.getPrice().subtract(item.getReduceAmount()).multiply(new BigDecimal(item.getQuantity())));
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) total = total.add(item.getPrice().subtract(item.getReduceAmount()).multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) total = total.add(item.getPrice().subtract(item.getReduceAmount()).multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }
}
