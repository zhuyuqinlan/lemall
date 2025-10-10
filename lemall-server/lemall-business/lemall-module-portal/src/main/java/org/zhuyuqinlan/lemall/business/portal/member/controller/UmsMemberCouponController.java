package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.OmsCartItemServicePortal;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberCouponService;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "用户优惠券管理", description = "UmsMemberCouponController")
@RequestMapping("${lemall.server.prefix.portal}/member/coupon")
public class UmsMemberCouponController {
    private final UmsMemberService umsMemberService;
    private final OmsCartItemServicePortal omsCartItemServicePortal;
    private final UmsMemberCouponService umsMemberCouponService;

    @Operation(summary = "领取指定优惠券")
    @PostMapping("/add/{couponId}")
    public Result<?> add(@PathVariable("couponId") Long couponId) {
        umsMemberCouponService.add(couponId);
        return Result.success();
    }

    @Operation(summary = "获取会员优惠券历史列表")
    @Parameter(name = "useStatus", description = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            in = ParameterIn.QUERY, schema = @Schema(type = "integer", allowableValues = {"0", "1", "2"}))
    @GetMapping("/listHistory")
    public Result<List<SmsCouponHistoryResponseDTO>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistoryResponseDTO> couponResponseDTOList = umsMemberCouponService.listHistory(useStatus);
        return Result.success(couponResponseDTOList);
    }

    @Operation(summary = "获取会员优惠券列表")
    @Parameter(name = "useStatus", description = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            in = ParameterIn.QUERY, schema = @Schema(type = "integer", allowableValues = {"0", "1", "2"}))
    @GetMapping("/list")
    public Result<List<SmsCouponResponseDTO>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponResponseDTO> couponResponseDTOList = umsMemberCouponService.list(useStatus);
        return Result.success(couponResponseDTOList);
    }

    @Operation(summary = "获取登录会员购物车的相关优惠券")
    @Parameter(name = "type", description = "使用可用:0->不可用；1->可用",
            in = ParameterIn.PATH, schema = @Schema(type = "integer", defaultValue = "1", allowableValues = {"0", "1"}))
    @GetMapping("/list/cart/{type}")
    public Result<List<SmsCouponHistoryDetail>> listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItems = omsCartItemServicePortal.listPromotion(umsMemberService.getCurrentMember().getId(), null);
        List<SmsCouponHistoryDetail> couponHistoryList = umsMemberCouponService.listCart(cartPromotionItems, type);
        return Result.success(couponHistoryList);
    }

    @Operation(summary = "获取当前商品相关优惠券")
    @GetMapping("/listByProduct/{productId}")
    public Result<List<SmsCouponResponseDTO>> listByProduct(@PathVariable Long productId) {
        List<SmsCouponResponseDTO> couponResponseDTOList = umsMemberCouponService.listByProduct(productId);
        return Result.success(couponResponseDTOList);
    }

}
