package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.*;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.*;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberCouponService;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.response.BizException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    private final UmsMemberService memberService;
    private final SmsCouponMapper couponMapperPortal;
    private final SmsCouponHistoryMapper couponHistoryMapperPortal;
    private final SmsCouponProductRelationMapper couponProductRelationMapper;
    private final SmsCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;
    private final PmsProductMapper productMapper;

    @Override
    public void add(Long couponId) {
        //获取优惠券信息，判断数量
        SmsCoupon smsCoupon = couponMapperPortal.selectById(couponId);
        if (smsCoupon == null) {
            throw new BizException("优惠劵不存在");
        }
        if (smsCoupon.getCount() <= 0) {
            throw new BizException("优惠劵已经领完了");
        }
        Date now = new Date();
        if (now.before(smsCoupon.getEnableTime())) {
            throw new BizException("优惠劵还没到领取时间");
        }
        // 判断用户领取的优惠劵数量是否超过限制
        Long count = couponHistoryMapperPortal.selectCount(Wrappers.<SmsCouponHistory>lambdaQuery()
                .eq(SmsCouponHistory::getCouponId, couponId)
        );
        if (count >= smsCoupon.getPerLimit()) {
            throw new BizException("您已经领取过该优惠劵");
        }
        // 生成领取优惠劵历史
        UmsMemberResponseDTO currentMember = memberService.getCurrentMember();
        SmsCouponHistory smsCouponHistory = new SmsCouponHistory();
        smsCouponHistory.setCouponId(couponId);
        smsCouponHistory.setCreateTime(now);
        smsCouponHistory.setMemberId(currentMember.getId());
        smsCouponHistory.setMemberNickname(currentMember.getNickname());
        // 主动领取
        smsCouponHistory.setGetType(1);
        // 未使用
        smsCouponHistory.setUseStatus(0);
        couponHistoryMapperPortal.insert(smsCouponHistory);
        // 修改优惠劵数量，领取数量
        smsCoupon.setCount(smsCoupon.getCount() - 1);
        smsCoupon.setReceiveCount(smsCoupon.getReceiveCount() == null ? 1 : smsCoupon.getReceiveCount() + 1);
        couponMapperPortal.updateById(smsCoupon);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }

    @Override
    public List<SmsCouponHistoryResponseDTO> listHistory(Integer useStatus) {
        List<SmsCouponHistory> smsCouponHistories = couponHistoryMapperPortal.selectList(Wrappers.<SmsCouponHistory>lambdaQuery()
                .eq(SmsCouponHistory::getMemberId, StpMemberUtil.getLoginId())
                .eq(useStatus != null, SmsCouponHistory::getUseStatus, useStatus)
        );
        List<SmsCouponHistoryResponseDTO> smsCouponHistoryResponseDTOList = new ArrayList<>();
        BeanUtils.copyProperties(smsCouponHistories, smsCouponHistoryResponseDTOList);
        return smsCouponHistoryResponseDTOList;
    }

    @Override
    public List<SmsCouponResponseDTO> list(Integer useStatus) {
        return couponHistoryMapperPortal.getCouponList(Long.parseLong(StpMemberUtil.getLoginId().toString()), useStatus);
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartPromotionItems, Integer type) {
        Date now = new Date();
        //获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryMapperPortal.getDetailList(Long.parseLong(StpMemberUtil.getLoginId().toString()));
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(0)) {
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartPromotionItems);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelationResponseDTO categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartPromotionItems, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelationResponseDTO productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartPromotionItems, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if (type.equals(1)) {
            return enableList;
        } else {
            return disableList;
        }
    }

    @Override
    public List<SmsCouponResponseDTO> listByProduct(Long productId) {
        List<Long> allCouponIds = new ArrayList<>();
        //获取指定商品优惠券
        List<SmsCouponProductRelation> cprList = couponProductRelationMapper.selectList(Wrappers.<SmsCouponProductRelation>lambdaQuery()
                .eq(SmsCouponProductRelation::getProductId, productId)
        );
        if (CollUtil.isNotEmpty(cprList)) {
            List<Long> couponIds = cprList.stream().map(SmsCouponProductRelation::getCouponId).toList();
            allCouponIds.addAll(couponIds);
        }
        //获取指定分类优惠券
        PmsProduct product = productMapper.selectById(productId);
        List<SmsCouponProductCategoryRelation> cpcrList = couponProductCategoryRelationMapper.selectList(Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery()
                .eq(SmsCouponProductCategoryRelation::getProductCategoryId, product.getProductCategoryId())
        );
        if (CollUtil.isNotEmpty(cpcrList)) {
            List<Long> couponIds = cpcrList.stream().map(SmsCouponProductCategoryRelation::getCouponId).collect(Collectors.toList());
            allCouponIds.addAll(couponIds);
        }
        if (CollUtil.isEmpty(allCouponIds)) {
            return new ArrayList<>();
        }
        //所有优惠券
        Date now = new Date();
        return couponMapperPortal.selectList(
                        Wrappers.<SmsCoupon>lambdaQuery()
                                .and(w -> w
                                        .lt(SmsCoupon::getEndTime, now)
                                        .gt(SmsCoupon::getStartTime, now)
                                        .eq(SmsCoupon::getUseType, 0))
                                .or(w -> w
                                        .lt(SmsCoupon::getEndTime, now)
                                        .gt(SmsCoupon::getStartTime, now)
                                        .ne(SmsCoupon::getUseType, 0)
                                        .in(SmsCoupon::getId, allCouponIds))
                ).stream()
                .map(e -> {
                    SmsCouponResponseDTO dto = new SmsCouponResponseDTO();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                })
                .toList();
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
