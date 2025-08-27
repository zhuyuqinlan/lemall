package org.nanguo.lemall.business.admin.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderReturnReasonRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnReasonResponseDTO;
import org.nanguo.lemall.business.admin.order.service.OmsOrderReturnReasonService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/lemall-admin/order/returnReason")
@Tag(name = "退货原因管理", description = "OmsOrderReturnReasonController")
public class OmsOrderReturnReasonController {

    private final OmsOrderReturnReasonService orderReturnReasonService;

    @Operation(summary = "添加退货原因")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody OmsOrderReturnReasonRequestDTO returnReason) {
        boolean b = orderReturnReasonService.create(returnReason);
        return b ? Result.success() : Result.fail("添加退货原因失败");
    }

    @Operation(summary = "修改退货原因")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @Validated @RequestBody OmsOrderReturnReasonRequestDTO returnReason) {
        boolean b = orderReturnReasonService.updateReason(id, returnReason);
        return b ? Result.success() : Result.fail("修改退货原因失败");
    }

    @Operation(summary = "批量删除退货原因")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = orderReturnReasonService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除退货原因失败");
    }

    @Operation(summary = "分页查询全部退货原因")
    @GetMapping("/list")
    public Result<IPage<OmsOrderReturnReasonResponseDTO>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<OmsOrderReturnReasonResponseDTO> reasonList = orderReturnReasonService.listPage(pageSize, pageNum);
        return Result.success(reasonList);
    }

    @Operation(summary = "获取单个退货原因详情信息")
    @GetMapping("/{id}")
    public Result<OmsOrderReturnReasonResponseDTO> getItem(@PathVariable Long id) {
        OmsOrderReturnReasonResponseDTO reason = orderReturnReasonService.getItem(id);
        return Result.success(reason);
    }

    @Operation(summary = "修改退货原因启用状态")
    @PostMapping("/update/status")
    public Result<?> updateStatus(@RequestParam(value = "status") Integer status,
                                     @RequestParam("ids") List<Long> ids) {
        boolean b = orderReturnReasonService.updateStatus(ids, status);
        return b ? Result.success() : Result.fail("修改退货原因启用状态失败");
    }
}
