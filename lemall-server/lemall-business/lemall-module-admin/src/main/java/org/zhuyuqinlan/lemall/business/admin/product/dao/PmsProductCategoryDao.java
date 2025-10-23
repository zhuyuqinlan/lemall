package org.zhuyuqinlan.lemall.business.admin.product.dao;

import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

public interface PmsProductCategoryDao {
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
