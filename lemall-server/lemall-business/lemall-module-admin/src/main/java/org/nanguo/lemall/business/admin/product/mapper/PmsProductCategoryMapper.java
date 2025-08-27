package org.nanguo.lemall.business.admin.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nanguo.lemall.business.admin.product.dto.response.PmsProductCategoryWithChildrenItem;
import org.nanguo.lemall.common.entity.PmsProductCategory;

import java.util.List;

public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}