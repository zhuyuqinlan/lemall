package org.zhuyuqinlan.lemall.business.admin.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductQueryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductParamResultDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductDTO;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/product/product")
@Tag(name = "商品管理",description = "PmsProductController")
public class PmsProductController {

    private final PmsProductService pmsProductService;

    @Operation(summary = "查询商品")
    @GetMapping("/list")
    public Result<IPage<PmsProductDTO>> getList(PmsProductQueryParamRequestDTO productQueryParam,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<PmsProductDTO> res = pmsProductService.getList(productQueryParam,pageSize,pageNum);
        return Result.success(res);
    }

    @Operation(summary = "创建商品")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody PmsProductParamRequestDTO requestDTO) {
        boolean flag = pmsProductService.create(requestDTO);
        return flag ? Result.success() : Result.fail("创建商品失败");
    }

    @Operation(summary = "根据商品id获取商品编辑信息")
    @GetMapping("/updateInfo/{id}")
    public Result<PmsProductParamResultDTO> updateInfo(@PathVariable("id") Long id) {
        PmsProductParamResultDTO res = pmsProductService.getUpdateInfo(id);
        return Result.success(res);
    }

    @Operation(summary = "更新商品")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @Validated @RequestBody PmsProductParamRequestDTO requestDTO) {
        boolean b = pmsProductService.updateProduct(id,requestDTO);
        return b ? Result.success() : Result.fail("更新商品失败");
    }

    @Operation(summary = "根据商品名称或货号模糊查询")
    @GetMapping("/simpleList")
    public Result<List<PmsProductDTO>> getSimpleList(String keyword) {
        List<PmsProductDTO> res = pmsProductService.getSimpleList(keyword);
        return Result.success(res);
    }

    @Operation(summary = "批量修改审核状态")
    @PostMapping("/update/verifyStatus")
    public Result<?> updateVerifyStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("verifyStatus") Integer verifyStatus,
                                        @RequestParam("detail") String detail) {
        boolean b = pmsProductService.updateVerifyStatus(ids,verifyStatus,detail);
        return b ? Result.success() : Result.fail("批量修改审核状态失败");
    }

    @Operation(summary = "批量上下架")
    @PostMapping("/update/publishStatus")
    public Result<?> updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                         @RequestParam("publishStatus") Integer publishStatus) {
        boolean b = pmsProductService.updatePublishStatus(ids,publishStatus);
        return b ? Result.success() : Result.fail("批量上下架失败");
    }

    @Operation(summary = "批量推荐商品")
    @PostMapping("/update/recommendStatus")
    public Result<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("recommendStatus") Integer recommendStatus) {
        boolean b = pmsProductService.updateRecommendStatus(ids,recommendStatus);
        return b ? Result.success() : Result.fail("批量推荐商品失败");
    }

    @Operation(summary = "批量设为新品")
    @PostMapping("/update/newStatus")
    public Result<?> updateNewStatus(@RequestParam("ids") List<Long> ids,
                                     @RequestParam("newStatus") Integer newStatus) {
        boolean b = pmsProductService.updateNewStatus(ids,newStatus);
        return b ? Result.success() : Result.fail("批量设为新品失败");
    }

    @Operation(summary = "批量修改删除状态")
    @PostMapping("/update/deleteStatus")
    public Result<?> updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("deleteStatus") Integer deleteStatus) {
        boolean b = pmsProductService.updateDeleteStatus(ids,deleteStatus);
        return b ? Result.success() : Result.fail("批量修改删除状态失败");
    }
}
