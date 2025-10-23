package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeNewProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeNewProductDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeNewProductService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/home/newProduct")
@Tag(name = "首页新品管理", description = "SmsHomeNewProductController")
public class SmsHomeNewProductController {

    private final SmsHomeNewProductService homeNewProductService;

    @Operation(summary = "添加首页推荐品牌")
    @PostMapping("/create")
    public Result<?> create(@RequestBody List<SmsHomeNewProductRequestDTO> homeBrandList) {
        boolean b = homeNewProductService.create(homeBrandList);
        return b ? Result.success() : Result.fail("添加首页推荐品牌失败");
    }

    @Operation(summary = "修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Result<?> updateSort(@PathVariable Long id, Integer sort) {
        boolean b = homeNewProductService.updateSort(id, sort);
        return b ? Result.success() : Result.fail("修改推荐排序失败");
    }

    @Operation(summary = "批量删除推荐")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = homeNewProductService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除推荐失败");
    }

    @Operation(summary = "批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Result<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        boolean b = homeNewProductService.updateRecommendStatus(ids, recommendStatus);
        return b ? Result.success() : Result.fail("批量删除推荐失败");
    }

    @Operation(summary = "分页查询推荐")
    @GetMapping("/list")
    public Result<IPage<SmsHomeNewProductDTO>> list(@RequestParam(value = "productName", required = false) String productName,
                                                    @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsHomeNewProductDTO> homeBrandList = homeNewProductService.listPage(productName, recommendStatus, pageSize, pageNum);
        return Result.success(homeBrandList);
    }
}
