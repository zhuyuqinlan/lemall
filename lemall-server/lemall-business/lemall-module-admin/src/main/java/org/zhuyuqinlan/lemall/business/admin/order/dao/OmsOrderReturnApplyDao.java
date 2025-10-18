package org.zhuyuqinlan.lemall.business.admin.order.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;

public interface OmsOrderReturnApplyDao {
    /**
     *获取申请详情
     * @param id id
     * @return 结果
     */
    OmsOrderReturnApplyResultResponseDTO getDetail(@Param("id") Long id);
}