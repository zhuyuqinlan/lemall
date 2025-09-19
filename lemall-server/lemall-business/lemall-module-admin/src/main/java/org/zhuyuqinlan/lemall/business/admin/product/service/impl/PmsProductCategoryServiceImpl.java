package org.zhuyuqinlan.lemall.business.admin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductCategoryResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductCategoryWithChildrenItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategoryAttributeRelation;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductCategoryAttributeRelationService;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.product.mapper.PmsProductCategoryMapper;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategory;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductCategoryService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService{

    private final PmsProductCategoryAttributeRelationService productCategoryAttributeRelationService;
    private final PmsProductService productService;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return baseMapper.listWithChildren();
    }

    @Override
    public boolean create(PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        pmsProductCategoryRequestDTO.setId(null);
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryRequestDTO, pmsProductCategory);
        //没有父分类时为一级分类
        setCategoryLevel(pmsProductCategory);
        boolean b = super.save(pmsProductCategory);
        List<Long> productAttributeIdList = pmsProductCategoryRequestDTO.getProductAttributeIdList();
        //创建筛选属性关联
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(pmsProductCategory.getId(), productAttributeIdList);
        }
        return b;
    }

    @Override
    public boolean updateCategory(Long id, PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        pmsProductCategoryRequestDTO.setId(null);
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        BeanUtils.copyProperties(pmsProductCategoryRequestDTO, pmsProductCategory);
        setCategoryLevel(pmsProductCategory);
        //更新商品分类时要更新商品中的名称
        productService.update(Wrappers.<PmsProduct>lambdaUpdate()
                .eq(PmsProduct::getProductCategoryId,id)
                .set(PmsProduct::getProductCategoryName,pmsProductCategory.getName())
        );
        //同时更新筛选属性的信息
        if(!CollectionUtils.isEmpty(pmsProductCategoryRequestDTO.getProductAttributeIdList())){
            productCategoryAttributeRelationService.remove(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>().eq(PmsProductCategoryAttributeRelation::getProductCategoryId,id));
            insertRelationList(id,pmsProductCategoryRequestDTO.getProductAttributeIdList());
        }else{
            productCategoryAttributeRelationService.remove(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>().eq(PmsProductCategoryAttributeRelation::getProductCategoryId,id));
        }
        return super.updateById(pmsProductCategory);
    }

    @Override
    public IPage<PmsProductCategoryResponseDTO> getList(Long parentId, Integer pageNum, Integer pageSize) {
        return super.page(new Page<>(pageNum,pageSize),Wrappers.<PmsProductCategory>lambdaQuery()
                .eq(PmsProductCategory::getParentId,parentId)
                .orderByDesc(PmsProductCategory::getSort)
        ).convert(e -> {
            PmsProductCategoryResponseDTO pmsProductCategoryResponseDTO = new PmsProductCategoryResponseDTO();
            BeanUtils.copyProperties(e, pmsProductCategoryResponseDTO);
            return pmsProductCategoryResponseDTO;
        });
    }

    @Override
    public PmsProductCategoryResponseDTO getItemById(Long id) {
        PmsProductCategoryResponseDTO pmsProductCategoryResponseDTO = new PmsProductCategoryResponseDTO();
        BeanUtils.copyProperties(super.getById(id),pmsProductCategoryResponseDTO);
        return pmsProductCategoryResponseDTO;
    }

    @Override
    public boolean deleteCategoryById(Long id) {
        // TODO 商品表关联了这个，删除时要确认
        return super.removeById(id);
    }

    @Override
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
        return super.update(Wrappers.<PmsProductCategory>lambdaUpdate()
                .in(PmsProductCategory::getId,ids)
                .set(PmsProductCategory::getNavStatus,navStatus)
        );
    }

    @Override
    public boolean updateShowStatus(List<Long> ids, Integer showStatus) {
        return super.update(Wrappers.<PmsProductCategory>lambdaUpdate()
                .in(PmsProductCategory::getId,ids)
                .set(PmsProductCategory::getShowStatus,showStatus)
        );
    }

    /**
     * 根据分类的parentId设置分类的level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        //没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            //有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory = super.getById(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }

    /**
     * 批量插入商品分类与筛选属性关系表
     * @param productCategoryId 商品分类id
     * @param productAttributeIdList 相关商品筛选属性id集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationService.saveBatch(relationList);
    }
}
