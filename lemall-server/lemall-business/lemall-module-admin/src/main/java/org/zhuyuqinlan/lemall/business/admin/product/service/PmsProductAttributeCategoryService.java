package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProductAttributeCategory;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductAttributeCategoryMapper;

import java.util.List;

@Service
public class PmsProductAttributeCategoryService extends ServiceImpl<PmsProductAttributeCategoryMapper, PmsProductAttributeCategory> {
    /**
     * 添加商品属性分类
     */
    public boolean create(String name) {
        return false;
    }

    /**
     * 修改商品属性分类
     */
    public boolean updateProduct(Long id, String name) {
        return false;
    }

    /**
     * 删除单个商品属性分类
     */
    public boolean delete(Long id) {
        return false;
    }

    /**
     * 获取单个商品属性分类信息
     */
    public PmsProductAttributeCategoryDTO getItem(Long id) {
        return null;
    }

    /**
     * 分页获取所有商品属性分类
     */
    public IPage<PmsProductAttributeCategoryDTO> getList(Integer pageSize, Integer pageNum) {
        return null;
    }

    /**
     * 获取所有商品属性分类及其下属性
     */
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return null;
    }
}
