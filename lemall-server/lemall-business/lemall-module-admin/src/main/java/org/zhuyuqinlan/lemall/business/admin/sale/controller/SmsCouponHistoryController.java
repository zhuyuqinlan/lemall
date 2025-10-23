package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsCouponHistoryDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponHistoryService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/couponHistory")
@Tag(name = "优惠券领取记录管理", description = "SmsCouponHistoryController")
public class SmsCouponHistoryController {

    private final SmsCouponHistoryService historyService;

    @Operation(summary = "根据优惠券id，使用状态，订单编号分页获取领取记录")
    @GetMapping("/list")
    public Result<PageResult<SmsCouponHistoryDTO>> list(@RequestParam(value = "couponId", required = false) Long couponId,
                                                        @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                                        @RequestParam(value = "orderSn", required = false) String orderSn,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsCouponHistoryDTO> historyList = historyService.listPage(couponId, useStatus, orderSn, pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(historyList));
    }
}
