package org.zhuyuqinlan.lemall.business.portal.member.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.portal.member.dto.CartProduct;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.OmsCartItemRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.OmsCartItemResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsCartItem;
import org.zhuyuqinlan.lemall.common.mapper.OmsCartItemMapper;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OmsCartItemService extends ServiceImpl<OmsCartItemMapper, OmsCartItem> {

    private final OmsPromotionService promotionService;

    /**
     * 获取包含促销活动信息的购物车列表
     */
    public List<CartPromotionItem> listPromotion(Long id, List<Long> cartIds) {
        List<OmsCartItemResponseDTO> omsCartItemResponseDTOS = listCartItemResponseDTO(id);
        if (CollUtil.isNotEmpty(omsCartItemResponseDTOS)) {
            omsCartItemResponseDTOS = omsCartItemResponseDTOS.stream().filter(item -> cartIds.contains(item.getId())).toList();
        }
        List<CartPromotionItem> cartPromotionItems = new ArrayList<>();
        if (!CollectionUtil.isEmpty(omsCartItemResponseDTOS)) {
            cartPromotionItems = promotionService.calcCartPromotion(omsCartItemResponseDTOS.stream().map(e -> {
                OmsCartItemRequestDTO cartItemRequestDTO = new OmsCartItemRequestDTO();
                BeanUtils.copyProperties(e, cartItemRequestDTO);
                return cartItemRequestDTO;
            }).toList());
        }
        return cartPromotionItems;
    }

    /**
     * 根据会员编号获取购物车列表
     */
    public List<OmsCartItemResponseDTO> listCartItemResponseDTO(Long memberId) {
        return super.list(Wrappers.<OmsCartItem>lambdaQuery()
                .eq(OmsCartItem::getDeleteStatus, 0)
                .eq(OmsCartItem::getMemberId, memberId)
        ).stream().map(e -> {
            OmsCartItemResponseDTO responseDTO = new OmsCartItemResponseDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        }).toList();
    }

    /**
     * 添加商品到购物车
     */
    public boolean add(OmsCartItemRequestDTO cartItem) {
        return false;
    }

    /**
     * 修改购物车中某个商品的数量
     */
    public boolean updateQuantity(Long id, long l, Integer quantity) {
        return false;
    }

    /**
     * 获取购物车中某个商品的规格,用于重选规格
     */
    public CartProduct getCartProduct(Long productId) {
        return null;
    }

    /**
     * 修改购物车中商品的规格
     */
    public boolean updateAttr(OmsCartItemRequestDTO cartItem) {
        return false;
    }

    /**
     * 删除购物车中的某个商品
     */
    public boolean delete(long l, List<Long> ids) {
        return false;
    }

    /**
     * 清空购物车
     */
    public boolean clear(long l) {
        return false;
    }
}
