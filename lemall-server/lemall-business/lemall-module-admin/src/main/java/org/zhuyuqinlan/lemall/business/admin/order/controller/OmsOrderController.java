package org.zhuyuqinlan.lemall.business.admin.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsMoneyInfoParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderQueryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsReceiverInfoParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderDetailDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderDTO;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsOrderService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/order/order")
@Tag(name = "订单管理", description = "OmsOrderController")
public class OmsOrderController {

    private final OmsOrderService orderService;

    @Operation(summary = "查询订单")
    @GetMapping("/list")
    public Result<PageResult<OmsOrderDTO>> list(@Validated OmsOrderQueryParamRequestDTO queryParam,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<OmsOrderDTO> orderList = orderService.listPage(queryParam, pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(orderList));
    }

    @Operation(summary = "批量发货")
    @PostMapping("/update/delivery")
    public Result<?> delivery(@RequestBody @Validated List<OmsOrderDeliveryParamRequestDTO> deliveryParamList) {
        boolean b = orderService.delivery(deliveryParamList);
        return b ? Result.success() : Result.fail("批量发货失败");
    }

    @Operation(summary = "批量关闭订单")
    @PostMapping("/update/close")
    public Result<?> close(@RequestParam("ids") List<Long> ids, @RequestParam String note) {
        boolean b = orderService.close(ids, note);
        return b ? Result.success() : Result.fail("批量关闭订单失败");
    }

    @Operation(summary = "批量删除订单")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = orderService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除订单失败");
    }

    @Operation(summary = "获取订单详情:订单信息、商品信息、操作记录")
    @GetMapping("/{id}")
    public Result<OmsOrderDetailDTO> detail(@PathVariable Long id) {
        OmsOrderDetailDTO orderDetailResult = orderService.detail(id);
        return Result.success(orderDetailResult);
    }

    @Operation(summary = "修改收货人信息")
    @PostMapping("/update/receiverInfo")
    public Result<?> updateReceiverInfo(@Validated @RequestBody OmsReceiverInfoParamRequestDTO receiverInfoParam) {
        boolean b = orderService.updateReceiverInfo(receiverInfoParam);
        return b ? Result.success() : Result.fail("修改收货人信息失败");
    }

    @Operation(summary = "修改订单费用信息")
    @PostMapping("/update/moneyInfo")
    public Result<?> updateReceiverInfo(@Validated @RequestBody OmsMoneyInfoParamRequestDTO moneyInfoParam) {
        boolean b = orderService.updateMoneyInfo(moneyInfoParam);
        return b ? Result.success() : Result.fail("修改订单费用信息失败");
    }

    @Operation(summary = "备注订单")
    @PostMapping("/update/note")
    public Result<?> updateNote(@RequestParam("id") Long id,
                                   @RequestParam("note") String note,
                                   @RequestParam("status") Integer status) {
        boolean b = orderService.updateNote(id, note, status);
        return b ? Result.success() : Result.fail("备注订单失败");
    }
}
