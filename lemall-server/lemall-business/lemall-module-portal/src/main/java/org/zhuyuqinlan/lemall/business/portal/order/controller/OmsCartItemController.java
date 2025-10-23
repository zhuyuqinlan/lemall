package org.zhuyuqinlan.lemall.business.portal.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.dto.CartProduct;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.OmsCartItemRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.OmsCartItemDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.OmsCartItemService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "购物车管理", description = "OmsCartItemController")
@RequestMapping("${lemall.server.prefix.portal}/order/cart")
public class OmsCartItemController {

    private final OmsCartItemService omsCartItemService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public Result<?> add(@RequestBody OmsCartItemRequestDTO cartItem) {
        boolean flag = omsCartItemService.add(cartItem);
        return flag ? Result.success() : Result.fail("添加商品到购物车失败");
    }

    @Operation(summary = "获取某个会员的购物车列表")
    @GetMapping("/list")
    public Result<List<OmsCartItemDTO>> list() {
        List<OmsCartItemDTO> cartItemList = omsCartItemService.listCartItemResponseDTO(Long.parseLong(StpMemberUtil.getLoginId().toString()));
        return Result.success(cartItemList);
    }

    @Operation(summary = "获取某个会员的购物车列表,包括促销信息")
    @GetMapping("/list/promotion")
    public Result<List<CartPromotionItem>> listPromotion(@RequestParam(required = false) List<Long> cartIds) {
        List<CartPromotionItem> cartPromotionItemList = omsCartItemService.listPromotion(Long.parseLong(StpMemberUtil.getLoginId().toString()), cartIds);
        return Result.success(cartPromotionItemList);
    }

    @Operation(summary = "修改购物车中某个商品的数量")
    @GetMapping("/update/quantity")
    public Result<?> updateQuantity(@RequestParam Long id,
                                          @RequestParam Integer quantity) {
        boolean flag = omsCartItemService.updateQuantity(id, Long.parseLong(StpMemberUtil.getLoginId().toString()), quantity);
        return flag ? Result.success() : Result.fail("修改购物车中某个商品的数量失败");
    }

    @Operation(summary = "获取购物车中某个商品的规格,用于重选规格")
    @GetMapping("/getProduct/{productId}")
    public Result<CartProduct> getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = omsCartItemService.getCartProduct(productId);
        return Result.success(cartProduct);
    }

    @Operation(summary = "修改购物车中商品的规格")
    @PostMapping("/update/attr")
    public Result<?> updateAttr(@RequestBody OmsCartItemRequestDTO cartItem) {
        boolean flag = omsCartItemService.updateAttr(cartItem);
        return flag ? Result.success() : Result.fail("修改购物车中商品的规格失败");
    }

    @Operation(summary = "删除购物车中的某个商品")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean flag = omsCartItemService.delete(Long.parseLong(StpMemberUtil.getLoginId().toString()), ids);
        return flag ? Result.success() : Result.fail("删除购物车中的某个商品失败");
    }

    @Operation(summary = "清空购物车")
    @PostMapping("/clear")
    public Result<?> clear() {
        boolean flag = omsCartItemService.clear(Long.parseLong(StpMemberUtil.getLoginId().toString()));
        return flag ? Result.success() : Result.fail("清空购物车失败");
    }
}
