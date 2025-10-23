package org.zhuyuqinlan.lemall.business.admin.product.dao;

import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductParamResultDTO;

public interface PmsProductDao {
    /**
     * 根据商品id获取商品编辑信息
     * @param id id
     * @return 结果
     */
    PmsProductParamResultDTO getUpdateInfo(Long id);
}
