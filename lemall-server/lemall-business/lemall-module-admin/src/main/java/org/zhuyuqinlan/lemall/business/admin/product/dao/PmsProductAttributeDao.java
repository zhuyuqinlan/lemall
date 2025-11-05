package org.zhuyuqinlan.lemall.business.admin.product.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.admin.product.dto.ProductAttrInfo;

import java.util.List;

/**
 * 自定义商品属性Dao
 */
public interface PmsProductAttributeDao {
    /**
     * 获取商品属性信息
     */
    List<ProductAttrInfo> getProductAttrInfo(@Param("id") Long productCategoryId);
}
