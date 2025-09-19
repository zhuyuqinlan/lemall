package org.zhuyuqinlan.lemall.business.admin.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductCategoryWithChildrenItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategory;

import java.util.List;

public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}