package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsCouponParamDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsCouponDTO;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/coupon")
@Tag(name = "优惠券管理", description = "SmsCouponController")
public class SmsCouponController {

    private final SmsCouponService couponService;

    @Operation(summary = "添加优惠券")
    @PostMapping("/create")
    public Result<?> add(@RequestBody SmsCouponParamRequestDTO couponParam) {
        boolean b = couponService.create(couponParam);
        return b ? Result.success() : Result.fail("添加优惠券失败");
    }

    @Operation(summary = "删除优惠券")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = couponService.delete(id);
        return b ? Result.success() : Result.fail("删除优惠券失败");
    }

    @Operation(summary = "修改优惠券")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id,@RequestBody SmsCouponParamRequestDTO couponParam) {
        boolean b = couponService.updateCoupon(id,couponParam);
        return b ? Result.success() : Result.fail("修改优惠券失败");
    }

    @Operation(summary = "根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping("/list")
    public Result<IPage<SmsCouponDTO>> list(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsCouponDTO> couponList = couponService.listPage(name,type,pageSize,pageNum);
        return Result.success(couponList);
    }

    @Operation(summary = "获取单个优惠券的详细信息")
    @GetMapping("/{id}")
    public Result<SmsCouponParamDTO> getItem(@PathVariable Long id) {
        SmsCouponParamDTO couponParam = couponService.getItem(id);
        return Result.success(couponParam);
    }
}
