package org.zhuyuqinlan.lemall.business.admin.product.dao;

import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductParamResultResponseDTO;

public interface PmsProductDao {
    /**
     * 根据商品id获取商品编辑信息
     * @param id id
     * @return 结果
     */
    PmsProductParamResultResponseDTO getUpdateInfo(Long id);
}