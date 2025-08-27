package org.nanguo.lemall.business.admin.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;
import org.nanguo.lemall.common.entity.OmsOrderReturnApply;

public interface OmsOrderReturnApplyMapper extends BaseMapper<OmsOrderReturnApply> {
    /**
     *获取申请详情
     * @param id id
     * @return 结果
     */
    OmsOrderReturnApplyResultResponseDTO getDetail(@Param("id") Long id);
}