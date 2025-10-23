package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.zhuyuqinlan.lemall.business.admin.product.dao.PmsProductCategoryDao;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductCategoryDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductCategoryWithChildrenItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategory;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategoryAttributeRelation;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductCategoryMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsProductCategoryService extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> {

    private final PmsProductCategoryAttributeRelationService productCategoryAttributeRelationService;
    private final PmsProductService productService;
    private final PmsProductCategoryDao productCategoryDao;

    /**
     * 查询所有一级分类及子分类
     */
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDao.listWithChildren();
    }

    /**
     * 添加产品分类
     */
    public boolean create(PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        pmsProductCategoryRequestDTO.setId(null);
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryRequestDTO, pmsProductCategory);
        setCategoryLevel(pmsProductCategory);
        boolean b = super.save(pmsProductCategory);

        List<Long> productAttributeIdList = pmsProductCategoryRequestDTO.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(pmsProductCategory.getId(), productAttributeIdList);
        }
        return b;
    }

    /**
     * 修改商品分类
     */
    public boolean updateCategory(Long id, PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        pmsProductCategoryRequestDTO.setId(null);
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        BeanUtils.copyProperties(pmsProductCategoryRequestDTO, pmsProductCategory);
        setCategoryLevel(pmsProductCategory);

        //更新商品分类名称
        productService.update(Wrappers.<PmsProduct>lambdaUpdate()
                .eq(PmsProduct::getProductCategoryId, id)
                .set(PmsProduct::getProductCategoryName, pmsProductCategory.getName())
        );

        //更新筛选属性
        if(!CollectionUtils.isEmpty(pmsProductCategoryRequestDTO.getProductAttributeIdList())){
            productCategoryAttributeRelationService.remove(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>()
                    .eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id));
            insertRelationList(id, pmsProductCategoryRequestDTO.getProductAttributeIdList());
        } else {
            productCategoryAttributeRelationService.remove(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>()
                    .eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id));
        }

        return super.updateById(pmsProductCategory);
    }

    /**
     * 分页查询商品分类
     */
    public IPage<PmsProductCategoryDTO> getList(Long parentId, Integer pageNum, Integer pageSize) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<PmsProductCategory>lambdaQuery()
                .eq(PmsProductCategory::getParentId, parentId)
                .orderByDesc(PmsProductCategory::getSort)
        ).convert(e -> {
            PmsProductCategoryDTO dto = new PmsProductCategoryDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }

    /**
     * 根据id获取商品分类
     */
    public PmsProductCategoryDTO getItemById(Long id) {
        PmsProductCategoryDTO dto = new PmsProductCategoryDTO();
        BeanUtils.copyProperties(super.getById(id), dto);
        return dto;
    }

    /**
     * 删除商品分类
     */
    public boolean deleteCategoryById(Long id) {
        return super.removeById(id);
    }

    /**
     * 修改导航栏显示状态
     */
    public boolean updateNavStatus(List<Long> ids, Integer navStatus) {
        return super.update(Wrappers.<PmsProductCategory>lambdaUpdate()
                .in(PmsProductCategory::getId, ids)
                .set(PmsProductCategory::getNavStatus, navStatus)
        );
    }

    /**
     * 修改显示状态
     */
    public boolean updateShowStatus(List<Long> ids, Integer showStatus) {
        return super.update(Wrappers.<PmsProductCategory>lambdaUpdate()
                .in(PmsProductCategory::getId, ids)
                .set(PmsProductCategory::getShowStatus, showStatus)
        );
    }

    private void setCategoryLevel(PmsProductCategory productCategory) {
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            PmsProductCategory parentCategory = super.getById(productCategory.getParentId());
            productCategory.setLevel(parentCategory != null ? parentCategory.getLevel() + 1 : 0);
        }
    }

    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long attrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductCategoryId(productCategoryId);
            relation.setProductAttributeId(attrId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationService.saveBatch(relationList);
    }
}
