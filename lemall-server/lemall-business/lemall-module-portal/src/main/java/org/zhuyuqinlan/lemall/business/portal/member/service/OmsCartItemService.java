package org.zhuyuqinlan.lemall.business.portal.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.common.entity.OmsCartItem;

import java.util.List;

public interface OmsCartItemService extends IService<OmsCartItem>{


    /**
     * 获取包含促销活动信息的购物车列表
     */
    List<CartPromotionItem> listPromotion(Long id, List<Long> cartIds);
}
