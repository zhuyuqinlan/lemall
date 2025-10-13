package org.zhuyuqinlan.lemall.business.portal.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponHistory;

import java.util.List;

public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {
    List<SmsCouponResponseDTO> getCouponList(@Param("memberId") Long loginId, @Param("useStatus") Integer useStatus);

    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
