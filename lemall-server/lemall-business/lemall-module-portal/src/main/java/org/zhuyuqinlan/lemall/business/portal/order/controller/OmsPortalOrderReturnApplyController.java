package org.zhuyuqinlan.lemall.business.portal.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhuyuqinlan.lemall.business.portal.order.dto.request.OmsOrderReturnApplyParam;
import org.zhuyuqinlan.lemall.business.portal.order.service.OmsPortalOrderReturnApplyService;
import org.zhuyuqinlan.lemall.common.response.Result;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "订单管理",description = "OmsPortalOrderReturnApplyController")
@RequestMapping("${lemall.server.prefix.portal}/order/returnApply")
public class OmsPortalOrderReturnApplyController {

    private final OmsPortalOrderReturnApplyService orderReturnApplyService;

    @Operation(summary = "申请退货")
    @PostMapping("/create")
    public Result<?> create(@RequestBody OmsOrderReturnApplyParam omsOrderReturnApplyParam) {
        boolean flag = orderReturnApplyService.create(omsOrderReturnApplyParam);
        return flag ? Result.success() : Result.fail("退货失败");
    }
}
