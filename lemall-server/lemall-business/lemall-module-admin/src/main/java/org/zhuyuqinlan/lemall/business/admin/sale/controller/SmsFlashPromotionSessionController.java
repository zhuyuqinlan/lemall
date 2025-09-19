package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionSessionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionDetailResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsFlashPromotionSessionService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/flashSession")
@Tag(name = "限时购场次管理", description = "SmsFlashPromotionSessionController")
public class SmsFlashPromotionSessionController {
    private final SmsFlashPromotionSessionService flashPromotionSessionService;

    @Operation(summary = "添加场次")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody SmsFlashPromotionSessionRequestDTO promotionSession) {
        boolean b = flashPromotionSessionService.create(promotionSession);
        return b ? Result.success() : Result.fail("添加场次失败");
    }

    @Operation(summary = "修改场次")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @Validated @RequestBody SmsFlashPromotionSessionRequestDTO promotionSession) {
        boolean b = flashPromotionSessionService.updateFlash(id, promotionSession);
        return b ? Result.success() : Result.fail("修改场次失败");
    }

    @Operation(summary = "修改启用状态")
    @PostMapping("/update/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, Integer status) {
        boolean b = flashPromotionSessionService.updateStatus(id, status);
        return b ? Result.success() : Result.fail("修改启用状态失败");
    }

    @Operation(summary = "删除场次")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = flashPromotionSessionService.delete(id);
        return b ? Result.success() : Result.fail("删除场次失败");
    }

    @Operation(summary = "获取场次详情")
    @GetMapping("/{id}")
    public Result<SmsFlashPromotionSessionResponseDTO> getItem(@PathVariable Long id) {
        SmsFlashPromotionSessionResponseDTO promotionSession = flashPromotionSessionService.getItem(id);
        return Result.success(promotionSession);
    }

    @Operation(summary = "获取全部场次")
    @GetMapping("/list")
    public Result<List<SmsFlashPromotionSessionResponseDTO>> list() {
        List<SmsFlashPromotionSessionResponseDTO> promotionSessionList = flashPromotionSessionService.listAll();
        return Result.success(promotionSessionList);
    }

    @Operation(summary = "获取全部可选场次及其数量")
    @GetMapping("/selectList")
    public Result<List<SmsFlashPromotionSessionDetailResponseDTO>> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetailResponseDTO> promotionSessionList = flashPromotionSessionService.selectList(flashPromotionId);
        return Result.success(promotionSessionList);
    }
}
