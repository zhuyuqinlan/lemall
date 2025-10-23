package org.zhuyuqinlan.lemall.business.portal.brand.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsBrandDTO;

import java.util.List;

@Service
public class PortalBrandService {
    /**
     * 分页获取推荐品牌
     */
    public List<PmsBrandDTO> recommendList(Integer pageNum, Integer pageSize) {
        return null;
    }

    /**
     * 获取品牌详情
     */
    public PmsBrandDTO detail(Long brandId) {
        return null;
    }

    /**
     * 分页获取品牌关联商品
     */
    public IPage<PmsBrandDTO> productList(Long brandId, Integer pageNum, Integer pageSize) {
        return null;
    }
}
