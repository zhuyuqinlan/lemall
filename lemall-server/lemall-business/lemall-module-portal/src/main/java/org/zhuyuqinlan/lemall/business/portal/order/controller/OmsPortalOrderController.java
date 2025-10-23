package org.zhuyuqinlan.lemall.business.portal.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.order.dto.ConfirmOrderResult;
import org.zhuyuqinlan.lemall.business.portal.order.dto.OmsOrderDetail;
import org.zhuyuqinlan.lemall.business.portal.order.dto.request.OrderParam;
import org.zhuyuqinlan.lemall.business.portal.order.service.OmsPortalOrderService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "订单管理",description = "OmsPortalOrderController")
@RequestMapping("${lemall.server.prefix.portal}/order/order")
public class OmsPortalOrderController {

    private final OmsPortalOrderService portalOrderService;

    @Operation(summary = "根据购物车信息生成确认单信息")
    @PostMapping("/generateConfirmOrder")
    public Result<ConfirmOrderResult> generateConfirmOrder(@RequestBody List<Long> cartIds) {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(cartIds);
        return Result.success(confirmOrderResult);
    }

    @Operation(summary = "根据购物车信息生成订单")
    @PostMapping("/generateOrder")
    public Result<Map<String, Object>> generateOrder(@RequestBody OrderParam orderParam) {
        Map<String, Object> result = portalOrderService.generateOrder(orderParam);
        return Result.success(result);
    }

    @Operation(summary = "用户支付成功的回调")
    @PostMapping("/paySuccess")
    public Result<?> paySuccess(@RequestParam Long orderId,@RequestParam Integer payType) {
        Integer count = portalOrderService.paySuccess(orderId,payType);
        return Result.success(count);
    }

    @Operation(summary = "自动取消超时订单")
    @PostMapping("/cancelTimeOutOrder")
    public Result<?> cancelTimeOutOrder() {
        portalOrderService.cancelTimeOutOrder();
        return Result.success();
    }

    @Operation(summary = "取消单个超时订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Result<?> cancelOrder(Long orderId) {
        portalOrderService.sendDelayMessageCancelOrder(orderId);
        return Result.success();
    }

    @Operation(summary = "按状态分页获取用户订单列表")
    @Parameter(name = "status", description = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            in = ParameterIn.QUERY, schema = @Schema(type = "integer",defaultValue = "-1",allowableValues = {"-1","0","1","2","3","4"}))
    @GetMapping("/list")
    public Result<PageResult<OmsOrderDetail>> list(@RequestParam Integer status,
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        IPage<OmsOrderDetail> orderPage = portalOrderService.list(status,pageNum,pageSize);
        return Result.success(PageResult.fromMybatis(orderPage));
    }

    @Operation(summary = "根据ID获取订单详情")
    @GetMapping("/detail/{orderId}")
    public Result<OmsOrderDetail> detail(@PathVariable Long orderId) {
        OmsOrderDetail orderDetail = portalOrderService.detail(orderId);
        return Result.success(orderDetail);
    }

    @Operation(summary = "用户取消订单")
    @PostMapping("/cancelUserOrder")
    public Result<?> cancelUserOrder(Long orderId) {
        portalOrderService.cancelOrder(orderId);
        return Result.success();
    }

    @Operation(summary = "用户确认收货")
    @PostMapping("/confirmReceiveOrder")
    public Result<?> confirmReceiveOrder(Long orderId) {
        portalOrderService.confirmReceiveOrder(orderId);
        return Result.success(null);
    }

    @Operation(summary = "用户删除订单")
    @PostMapping("/deleteOrder")
    public Result<?> deleteOrder(Long orderId) {
        portalOrderService.deleteOrder(orderId);
        return Result.success();
    }
}
