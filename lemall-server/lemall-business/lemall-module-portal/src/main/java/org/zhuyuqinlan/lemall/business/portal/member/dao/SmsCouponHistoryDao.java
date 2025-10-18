package org.zhuyuqinlan.lemall.business.portal.member.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;

import java.util.List;

public interface SmsCouponHistoryDao {
    List<SmsCouponResponseDTO> getCouponList(@Param("memberId") Long loginId, @Param("useStatus") Integer useStatus);

    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
