package org.nanguo.lemall.business.admin.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.nanguo.lemall.common.entity.SmsCoupon;

public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParamRequestDTO getItem(Long id);
}