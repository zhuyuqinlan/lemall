package org.zhuyuqinlan.lemall.business.admin.order.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderReturnApplyResultDTO;

public interface OmsOrderReturnApplyDao {
    /**
     *获取申请详情
     * @param id id
     * @return 结果
     */
    OmsOrderReturnApplyResultDTO getDetail(@Param("id") Long id);
}
