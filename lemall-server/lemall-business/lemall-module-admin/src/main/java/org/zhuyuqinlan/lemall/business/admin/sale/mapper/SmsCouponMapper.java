package org.zhuyuqinlan.lemall.business.admin.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;

public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParamRequestDTO getItem(Long id);
}