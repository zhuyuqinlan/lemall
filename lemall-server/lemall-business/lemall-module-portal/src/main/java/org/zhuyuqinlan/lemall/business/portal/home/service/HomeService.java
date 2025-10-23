package org.zhuyuqinlan.lemall.business.portal.home.service;

import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.home.dto.CmsSubjectDTO;
import org.zhuyuqinlan.lemall.business.portal.home.dto.HomeContentResult;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsProductCategoryDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;

import java.util.List;

@Service
public class HomeService {
    /**
     * 获取首页内容
     */
    public HomeContentResult content() {
        return null;
    }

    /**
     * 首页商品推荐
     */
    public List<PmsProductDTO> recommendProductList(Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 获取商品分类
     * @param parentId 0:获取一级分类；其他：获取指定二级分类
     */
    public List<PmsProductCategoryDTO> getProductCateList(Long parentId) {
        return null;
    }

    /**
     * 根据专题分类分页获取专题
     * @param cateId 专题分类id
     */
    public List<CmsSubjectDTO> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 分页获取人气推荐商品
     */
    public List<PmsProductDTO> hotProductList(Integer pageNum, Integer pageSize) {
        return null;
    }

    /**
     * 分页获取新品推荐商品
     */
    public List<PmsProductDTO> newProductList(Integer pageNum, Integer pageSize) {
        return null;
    }
}
