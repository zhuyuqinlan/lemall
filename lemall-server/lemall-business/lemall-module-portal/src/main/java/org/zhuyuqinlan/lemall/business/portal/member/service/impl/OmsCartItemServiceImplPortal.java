package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.OmsCartItemMapperPortal;
import org.zhuyuqinlan.lemall.business.portal.member.service.OmsCartItemServicePortal;
import org.zhuyuqinlan.lemall.common.entity.OmsCartItem;

import java.util.List;

@Service
public class OmsCartItemServiceImplPortal extends ServiceImpl<OmsCartItemMapperPortal, OmsCartItem> implements OmsCartItemServicePortal {

    @Override
    public List<CartPromotionItem> listPromotion(Long id, List<Long> cartIds) {
        return List.of();
    }
}
