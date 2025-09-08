package org.nanguo.lemall.business.admin.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.product.dto.request.PmsProductCategoryRequestDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsProductCategoryResponseDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsProductCategoryWithChildrenItem;
import org.nanguo.lemall.business.admin.product.service.PmsProductCategoryService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/product/productCategory")
@Tag(name = "商品分类管理",description = "PmsProductCategoryController")
public class PmsProductCategoryController {

    private final PmsProductCategoryService pmsProductCategoryService;

    @Operation(summary = "查询所有一级分类及子分类")
    @GetMapping("/list/withChildren")
    public Result<List<PmsProductCategoryWithChildrenItem>> listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = pmsProductCategoryService.listWithChildren();
        return Result.success(list);
    }

    @Operation(summary = "添加产品分类")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        boolean b = pmsProductCategoryService.create(pmsProductCategoryRequestDTO);
        return b ? Result.success() : Result.fail("添加产品分类失败");
    }

    @Operation(summary = "修改商品分类")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id,
                            @Validated
                            @RequestBody PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO) {
        boolean b = pmsProductCategoryService.updateCategory(id,pmsProductCategoryRequestDTO);
        return b ? Result.success() : Result.fail("修改商品分类失败");
    }

    @Operation(summary = "分页查询商品分类")
    @GetMapping("/list/{parentId}")
    public Result<IPage<PmsProductCategoryResponseDTO>> getList(@PathVariable Long parentId,
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<PmsProductCategoryResponseDTO> res = pmsProductCategoryService.getList(parentId,pageNum,pageSize);
        return Result.success(res);
    }

    @Operation(summary = "根据id获取商品分类")
    @GetMapping("/{id}")
    public Result<PmsProductCategoryResponseDTO> getById(@PathVariable Long id) {
        PmsProductCategoryResponseDTO res = pmsProductCategoryService.getItemById(id);
        return Result.success(res);
    }

    @Operation(summary = "删除商品分类")
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = pmsProductCategoryService.deleteCategoryById(id);
        return b ? Result.success() : Result.fail("删除商品分类失败");
    }

    @Operation(summary = "修改导航栏显示状态")
    @PostMapping("/update/navStatus")
    public Result<?> updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        boolean b = pmsProductCategoryService.updateNavStatus(ids,navStatus);
        return b ? Result.success() : Result.fail("修改导航栏显示状态失败");
    }

    @Operation(summary = "修改显示状态")
    @PostMapping("/update/showStatus")
    public Result<?> updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        boolean b = pmsProductCategoryService.updateShowStatus(ids,showStatus);
        return b ? Result.success() : Result.fail("修改显示状态失败");
    }
}
