package org.nanguo.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeRecommendProductRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeRecommendProductResponseDTO;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeRecommendProductService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/home/recommendProduct")
@Tag(name = "首页人气推荐管理", description = "SmsHomeRecommendProductController")
public class SmsHomeRecommendProductController {

    private final SmsHomeRecommendProductService recommendProductService;

    @Operation(summary = "添加首页推荐")
    @PostMapping("/create")
    public Result<?> create(@RequestBody List<SmsHomeRecommendProductRequestDTO> homeBrandList) {
        boolean b = recommendProductService.create(homeBrandList);
        return b ? Result.success() : Result.fail("添加首页推荐失败");
    }

    @Operation(summary = "修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Result<?> updateSort(@PathVariable Long id, Integer sort) {
        boolean b = recommendProductService.updateSort(id, sort);
        return b ? Result.success() : Result.fail("修改推荐排序失败");
    }

    @Operation(summary = "批量删除推荐")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = recommendProductService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除推荐失败");
    }

    @Operation(summary = "批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Result<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        boolean b = recommendProductService.updateRecommendStatus(ids, recommendStatus);
        return b ? Result.success() : Result.fail("批量修改推荐状态失败");
    }

    @Operation(summary = "分页查询推荐")
    @GetMapping("/list")
    public Result<IPage<SmsHomeRecommendProductResponseDTO>> list(@RequestParam(value = "productName", required = false) String productName,
                                                                  @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsHomeRecommendProductResponseDTO> homeBrandList = recommendProductService.listPage(productName, recommendStatus, pageSize, pageNum);
        return Result.success(homeBrandList);
    }
}
