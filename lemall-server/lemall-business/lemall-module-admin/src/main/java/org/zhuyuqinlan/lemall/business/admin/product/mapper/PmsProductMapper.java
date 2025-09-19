package org.zhuyuqinlan.lemall.business.admin.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductParamResultResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;

public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    /**
     * 根据商品id获取商品编辑信息
     * @param id id
     * @return 结果
     */
    PmsProductParamResultResponseDTO getUpdateInfo(Long id);
}