package org.nanguo.lemall.business.admin.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.order.dto.request.OmsReturnApplyQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsUpdateStatusParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResponseDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;
import org.nanguo.lemall.business.admin.order.service.OmsOrderReturnApplyService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/lemall-admin/order/returnApply")
@Tag(name = "订单退货申请管理", description = "OmsOrderReturnApplyController")
public class OmsOrderReturnApplyController {
    private final OmsOrderReturnApplyService returnApplyService;

    @Operation(summary = "分页查询退货申请")
    @GetMapping("/list")
    public Result<IPage<OmsOrderReturnApplyResponseDTO>> list(OmsReturnApplyQueryParamRequestDTO queryParam,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<OmsOrderReturnApplyResponseDTO> returnApplyList = returnApplyService.listPage(queryParam, pageSize, pageNum);
        return Result.success(returnApplyList);
    }

    @Operation(summary = "批量删除申请")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = returnApplyService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除申请失败");
    }

    @Operation(summary = "获取退货申请详情")
    @GetMapping("/{id}")
    public Result<?> getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResultResponseDTO result = returnApplyService.getItem(id);
        return Result.success(result);
    }

    @Operation(summary = "修改申请状态")
    @PostMapping("/update/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @Validated @RequestBody OmsUpdateStatusParamRequestDTO statusParam) {
        boolean b = returnApplyService.updateStatus(id, statusParam);
        return b ? Result.success() : Result.fail("修改申请状态失败");
    }
}
