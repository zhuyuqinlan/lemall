package org.zhuyuqinlan.lemall.business.admin.sale.dao;

import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponParamResponseDTO;

public interface SmsCouponDao {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParamResponseDTO getItem(Long id);
}