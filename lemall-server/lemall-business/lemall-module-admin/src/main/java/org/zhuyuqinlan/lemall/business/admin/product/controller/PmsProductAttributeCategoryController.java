package org.zhuyuqinlan.lemall.business.admin.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeCategoryItem;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductAttributeCategoryService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/product/productAttribute/category")
@Tag(name = "商品属性分类管理",description = "PmsProductAttributeCategoryController")
public class PmsProductAttributeCategoryController {

    private final PmsProductAttributeCategoryService productAttributeCategoryService;

    @Operation(summary = "添加商品属性分类")
    @PostMapping("/create")
    public Result<?> create(@RequestParam String name) {
        boolean flag = productAttributeCategoryService.create(name);
        return flag ? Result.success() : Result.fail("添加商品属性分类");
    }

    @Operation(summary = "修改商品属性分类")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestParam String name) {
        boolean flag = productAttributeCategoryService.updateProduct(id, name);
        return flag ? Result.success() : Result.fail("修改商品属性分类");
    }

    @Operation(summary = "删除单个商品属性分类")
    @GetMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean flag = productAttributeCategoryService.delete(id);
        return flag ? Result.success() : Result.fail("删除单个商品属性分类");
    }

    @Operation(summary = "获取单个商品属性分类信息")
    @GetMapping("/{id}")
    public Result<PmsProductAttributeCategoryDTO> getItem(@PathVariable Long id) {
        PmsProductAttributeCategoryDTO productAttributeCategory = productAttributeCategoryService.getItem(id);
        return Result.success(productAttributeCategory);
    }

    @Operation(summary = "分页获取所有商品属性分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<PageResult<PmsProductAttributeCategoryDTO>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        IPage<PmsProductAttributeCategoryDTO> productAttributeCategoryList = productAttributeCategoryService.getList(pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(productAttributeCategoryList));
    }

    @Operation(summary = "获取所有商品属性分类及其下属性")
    @RequestMapping(value = "/list/withAttr", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<PmsProductAttributeCategoryItem>> getListWithAttr() {
        List<PmsProductAttributeCategoryItem> productAttributeCategoryResultList = productAttributeCategoryService.getListWithAttr();
        return Result.success(productAttributeCategoryResultList);
    }
}
