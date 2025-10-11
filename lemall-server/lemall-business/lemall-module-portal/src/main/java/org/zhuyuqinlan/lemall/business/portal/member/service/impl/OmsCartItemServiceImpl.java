package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.OmsCartItemMapper;
import org.zhuyuqinlan.lemall.business.portal.member.service.OmsCartItemService;
import org.zhuyuqinlan.lemall.common.entity.OmsCartItem;

import java.util.List;

@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements OmsCartItemService {

    @Override
    public List<CartPromotionItem> listPromotion(Long id, List<Long> cartIds) {
        return List.of();
    }
}
