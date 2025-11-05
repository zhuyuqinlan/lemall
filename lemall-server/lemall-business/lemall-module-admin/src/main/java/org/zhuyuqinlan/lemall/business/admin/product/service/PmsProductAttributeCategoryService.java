package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.product.dao.PmsProductAttributeCategoryDao;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProductAttributeCategory;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductAttributeCategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductAttributeCategoryService extends ServiceImpl<PmsProductAttributeCategoryMapper, PmsProductAttributeCategory> {

    private final PmsProductAttributeCategoryDao pmsProductAttributeCategoryDao;
    /**
     * 添加商品属性分类
     */
    public boolean create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return super.save(productAttributeCategory);
    }

    /**
     * 修改商品属性分类
     */
    public boolean updateProduct(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return super.updateById(productAttributeCategory);
    }

    /**
     * 删除单个商品属性分类
     */
    public boolean delete(Long id) {
        return super.removeById(id);
    }

    /**
     * 获取单个商品属性分类信息
     */
    public PmsProductAttributeCategoryDTO getItem(Long id) {
        PmsProductAttributeCategory productAttributeCategory = getById(id);
        PmsProductAttributeCategoryDTO productAttributeCategoryDTO = new PmsProductAttributeCategoryDTO();
        BeanUtils.copyProperties(productAttributeCategory, productAttributeCategoryDTO);
        return productAttributeCategoryDTO;
    }

    /**
     * 分页获取所有商品属性分类
     */
    public IPage<PmsProductAttributeCategoryDTO> getList(Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize)).convert(e -> {
            PmsProductAttributeCategoryDTO productAttributeCategoryDTO = new PmsProductAttributeCategoryDTO();
            BeanUtils.copyProperties(e, productAttributeCategoryDTO);
            return productAttributeCategoryDTO;
        });
    }

    /**
     * 获取所有商品属性分类及其下属性
     */
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return pmsProductAttributeCategoryDao.getListWithAttr();
    }
}
