package org.zhuyuqinlan.lemall.business.portal.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;
import org.zhuyuqinlan.lemall.business.portal.product.dto.PmsPortalProductDetail;
import org.zhuyuqinlan.lemall.business.portal.product.dto.PmsProductCategoryNode;

import java.util.List;

@Service
public class PmsPortalProductService {
    /**
     * 综合搜索商品
     */
    public IPage<PmsProductDTO> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        return null;
    }

    /**
     * 以树形结构获取所有商品分类
     */
    public List<PmsProductCategoryNode> categoryTreeList() {
        return null;
    }

    /**
     * 获取前台商品详情
     */
    public PmsPortalProductDetail detail(Long id) {
        return null;
    }
}
