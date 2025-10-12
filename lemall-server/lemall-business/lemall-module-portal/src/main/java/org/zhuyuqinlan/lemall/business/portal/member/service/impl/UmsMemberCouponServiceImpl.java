package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponHistoryMapper;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponMapper;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberCouponService;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponHistory;
import org.zhuyuqinlan.lemall.common.response.BizException;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    private final UmsMemberService memberService;
    private final SmsCouponMapper couponMapperPortal;
    private final SmsCouponHistoryMapper couponHistoryMapperPortal;

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
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
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
