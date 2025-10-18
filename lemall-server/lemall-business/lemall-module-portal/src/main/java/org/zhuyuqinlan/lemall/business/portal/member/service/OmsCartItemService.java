package org.zhuyuqinlan.lemall.business.portal.member.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.common.entity.OmsCartItem;
import org.zhuyuqinlan.lemall.common.mapper.OmsCartItemMapper;

import java.util.List;


@Service
public class OmsCartItemService extends ServiceImpl<OmsCartItemMapper, OmsCartItem> {

    /**
     * 获取包含促销活动信息的购物车列表
     */
    public List<CartPromotionItem> listPromotion(Long id, List<Long> cartIds) {
        // TODO: 实现具体逻辑
        return List.of();
    }
}
