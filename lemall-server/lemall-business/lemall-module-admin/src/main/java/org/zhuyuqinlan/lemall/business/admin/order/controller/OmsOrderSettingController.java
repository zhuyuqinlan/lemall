package org.zhuyuqinlan.lemall.business.admin.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderSettingRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderSettingDTO;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsOrderSettingService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/order/orderSetting")
@Tag(name = "订单设置管理", description = "OmsOrderSettingController")
public class OmsOrderSettingController {

    private final OmsOrderSettingService orderSettingService;

    @Operation(summary = "获取指定订单设置")
    @GetMapping("/{id}")
    public Result<OmsOrderSettingDTO> getItem(@PathVariable Long id) {
        OmsOrderSettingDTO orderSetting = orderSettingService.getItem(id);
        return Result.success(orderSetting);
    }

    @Operation(summary = "修改指定订单设置")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody OmsOrderSettingRequestDTO orderSetting) {
        boolean b = orderSettingService.updateSetting(id,orderSetting);
        return b ? Result.success() : Result.fail("修改指定订单设置失败");
    }
}
