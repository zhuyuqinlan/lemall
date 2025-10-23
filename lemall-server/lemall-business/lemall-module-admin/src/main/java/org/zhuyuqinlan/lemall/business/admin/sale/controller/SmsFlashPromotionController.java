package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsFlashPromotionDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsFlashPromotionService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/flash")
@Tag(name = "限时购活动管理", description = "SmsFlashPromotionController")
public class SmsFlashPromotionController {

    private final SmsFlashPromotionService flashPromotionService;

    @Operation(summary = "添加活动")
    @PostMapping("/create")
    public Result<?> create(@RequestBody SmsFlashPromotionRequestDTO flashPromotion) {
        boolean b = flashPromotionService.create(flashPromotion);
        return b ? Result.success() : Result.fail("添加活动失败");
    }

    @Operation(summary = "编辑活动信息")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SmsFlashPromotionRequestDTO flashPromotion) {
        boolean b = flashPromotionService.updateFlash(id, flashPromotion);
        return b ? Result.success() : Result.fail("编辑活动信息失败");
    }

    @Operation(summary = "删除活动信息")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = flashPromotionService.delete(id);
        return b ? Result.success() : Result.fail("删除活动信息失败");
    }

    @Operation(summary = "修改上下线状态")
    @PostMapping("/update/status/{id}")
    public Result<?> update(@PathVariable Long id, Integer status) {
        boolean b = flashPromotionService.updateStatus(id, status);
        return b ? Result.success() : Result.fail("修改上下线状态失败");
    }

    @Operation(summary = "获取活动详情")
    @GetMapping("/{id}")
    public Result<SmsFlashPromotionDTO> getItem(@PathVariable Long id) {
        SmsFlashPromotionDTO flashPromotion = flashPromotionService.getItem(id);
        return Result.success(flashPromotion);
    }

    @Operation(summary = "根据活动名称分页查询")
    @GetMapping("/list")
    public Result<IPage<SmsFlashPromotionDTO>> getItem(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsFlashPromotionDTO> flashPromotionList = flashPromotionService.listPage(keyword, pageSize, pageNum);
        return Result.success(flashPromotionList);
    }
}
