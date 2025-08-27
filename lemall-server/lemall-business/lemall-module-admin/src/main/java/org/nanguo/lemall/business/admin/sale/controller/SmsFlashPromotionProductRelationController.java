package org.nanguo.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionProductRelationRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionProductRelationService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/lemall-admin/sale/flashProductRelation")
@Tag(name = "限时购和商品关系管理", description = "SmsFlashPromotionProductRelationController")
public class SmsFlashPromotionProductRelationController {
    private final SmsFlashPromotionProductRelationService relationService;

    @Operation(summary = "批量选择商品添加关联")
    @PostMapping("/create")
    public Result<?> create(@RequestBody List<SmsFlashPromotionProductRelationRequestDTO> relationList) {
        boolean b = relationService.create(relationList);
        return b ? Result.success() : Result.fail("批量选择商品添加关联失败");
    }

    @Operation(summary = "修改关联相关信息")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SmsFlashPromotionProductRelationRequestDTO relation) {
        boolean b = relationService.updatePro(id, relation);
        return b ? Result.success() : Result.fail("修改关联相关信息失败");
    }

    @Operation(summary = "删除关联")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = relationService.delete(id);
        return b ? Result.success() : Result.fail("删除关联失败");
    }

    @Operation(summary = "获取管理商品促销信息")
    @GetMapping("/{id}")
    public Result<SmsFlashPromotionProductRelationResponseDTO> getItem(@PathVariable Long id) {
        SmsFlashPromotionProductRelationResponseDTO relation = relationService.getItem(id);
        return Result.success(relation);
    }

    @Operation(summary = "分页查询不同场次关联及商品信息")
    @GetMapping("/list")
    public Result<IPage<SmsFlashPromotionProductRelationResponseDTO>> list(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                        @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsFlashPromotionProductRelationResponseDTO> flashPromotionProductList = relationService.listPage(flashPromotionId, flashPromotionSessionId, pageSize, pageNum);
        return Result.success(flashPromotionProductList);
    }
}
