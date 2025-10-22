package org.zhuyuqinlan.lemall.business.portal.member.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.portal.member.dto.CartProduct;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PromotionProduct;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;


import java.util.List;

public interface PortalProductDao {
    CartProduct getCartProduct(@Param("id") Long id);
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId")Long productCategoryId);
}
