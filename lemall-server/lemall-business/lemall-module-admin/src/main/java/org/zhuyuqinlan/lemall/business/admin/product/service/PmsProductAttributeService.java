package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.product.dao.PmsProductAttributeDao;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.ProductAttrInfo;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductAttributeParam;
import org.zhuyuqinlan.lemall.common.entity.PmsProductAttribute;
import org.zhuyuqinlan.lemall.common.entity.PmsProductAttributeCategory;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductAttributeCategoryMapper;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductAttributeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductAttributeService extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> {

    private final PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;
    private final PmsProductAttributeDao pmsProductAttributeDao;

    /**
     * 根据分类查询属性列表或参数列表
     */
    public IPage<PmsProductAttributeDTO> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<PmsProductAttribute>lambdaQuery()
                .eq(PmsProductAttribute::getProductAttributeCategoryId, cid)
                .eq(PmsProductAttribute::getType, type)
                .orderByDesc(PmsProductAttribute::getSort)
        ).convert(e -> {
            PmsProductAttributeDTO pmsProductAttributeDTO = new PmsProductAttributeDTO();
            BeanUtils.copyProperties(e, pmsProductAttributeDTO);
            return pmsProductAttributeDTO;
        });
    }


    /**
     * 添加商品属性信息
     */
    public boolean create(PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam, pmsProductAttribute);
        super.save(pmsProductAttribute);
        //新增商品属性以后需要更新商品属性分类数量
        PmsProductAttributeCategory pmsProductAttributeCategory = pmsProductAttributeCategoryMapper.selectById(productAttributeParam.getProductAttributeCategoryId());
        if (pmsProductAttribute.getType() == 0) {
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() + 1);
        } else if (pmsProductAttribute.getType() == 1) {
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() + 1);
        }
        pmsProductAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return true;
    }


    /**
     * 修改商品属性信息
     */
    public boolean updateAttr(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam, pmsProductAttribute);
        pmsProductAttribute.setId(id);
        return super.updateById(pmsProductAttribute);
    }


    /**
     * 查询单个商品属性
     */
    public PmsProductAttributeDTO getItem(Long id) {
        PmsProductAttribute pmsProductAttribute = getById(id);
        PmsProductAttributeDTO pmsProductAttributeDTO = new PmsProductAttributeDTO();
        BeanUtils.copyProperties(pmsProductAttribute, pmsProductAttributeDTO);
        return pmsProductAttributeDTO;
    }

    /**
     * 批量删除商品属性
     */
    public int delete(List<Long> ids) {
        //获取分类
        PmsProductAttribute pmsProductAttribute = super.getById(ids.get(0));
        Integer type = pmsProductAttribute.getType();
        PmsProductAttributeCategory pmsProductAttributeCategory = pmsProductAttributeCategoryMapper.selectById(pmsProductAttribute.getProductAttributeCategoryId());
        int count = super.getBaseMapper().delete(Wrappers.<PmsProductAttribute>lambdaQuery()
                .in(PmsProductAttribute::getProductAttributeCategoryId, ids)
        );
        //删除完成后修改数量
        if (type == 0) {
            if (pmsProductAttributeCategory.getAttributeCount() >= count) {
                pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() - count);
            } else {
                pmsProductAttributeCategory.setAttributeCount(0);
            }
        } else if (type == 1) {
            if (pmsProductAttributeCategory.getParamCount() >= count) {
                pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() - count);
            } else {
                pmsProductAttributeCategory.setParamCount(0);
            }
        }
        pmsProductAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return count;
    }

    /**
     * 根据商品分类的id获取商品属性及属性分类
     */
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return pmsProductAttributeDao.getProductAttrInfo(productCategoryId);
    }
}
