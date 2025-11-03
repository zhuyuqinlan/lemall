package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.ProductAttrInfo;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductAttributeParam;
import org.zhuyuqinlan.lemall.common.entity.PmsProductAttribute;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductAttributeMapper;

import java.util.List;

@Service
public class PmsProductAttributeService extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> {

    /**
     * 根据分类查询属性列表或参数列表
     */
    public IPage<PmsProductAttributeDTO> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return null;
    }


    /**
     * 添加商品属性信息
     */
    public boolean create(PmsProductAttributeParam productAttributeParam) {
        return false;
    }


    /**
     * 修改商品属性信息
     */
    public boolean updateAttr(Long id, PmsProductAttributeParam productAttributeParam) {
        return false;
    }


    /**
     * 查询单个商品属性
     */
    public PmsProductAttributeDTO getItem(Long id) {
        return null;
    }

    /**
     * 批量删除商品属性
     */
    public boolean delete(List<Long> ids) {
        return false;
    }

    /**
     * 根据商品分类的id获取商品属性及属性分类
     */
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return null;
    }
}
