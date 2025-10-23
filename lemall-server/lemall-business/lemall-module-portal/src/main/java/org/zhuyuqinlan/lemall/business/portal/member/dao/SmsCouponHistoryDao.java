package org.zhuyuqinlan.lemall.business.portal.member.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.portal.member.dto.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.SmsCouponDTO;

import java.util.List;

public interface SmsCouponHistoryDao {
    List<SmsCouponDTO> getCouponList(@Param("memberId") Long loginId, @Param("useStatus") Integer useStatus);

    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
