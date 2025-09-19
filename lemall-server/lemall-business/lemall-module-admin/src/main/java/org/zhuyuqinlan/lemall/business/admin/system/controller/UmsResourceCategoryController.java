package org.zhuyuqinlan.lemall.business.admin.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsResourceCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsResourceCategoryResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsResourceCategoryService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/system/resourceCategory")
@Tag(name = "资源分类管理")
public class UmsResourceCategoryController {

    private final UmsResourceCategoryService umsResourceCategoryService;

    @Operation(summary = "查询所有后台资源分类")
    @GetMapping("/listAll")
    public Result<List<UmsResourceCategoryResponseDTO>> listAllResourceCategory() {
        List<UmsResourceCategoryResponseDTO> umsResourceCategoryResponseDTOS = umsResourceCategoryService.listAllResourceCategory();
        return Result.success(umsResourceCategoryResponseDTOS);
    }

    @Operation(summary = "添加后台资源分类")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        boolean flag = umsResourceCategoryService.create(umsResourceCategoryRequestDTO);
        return flag ? Result.success() : Result.fail("添加资源分类失败");
    }

    @Operation(summary = "修改后台资源分类")
    @PostMapping("/update/{id}")
    public Result<?> update(@NotNull @PathVariable Long id,@Validated @RequestBody UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        boolean flag = umsResourceCategoryService.updateRes(id,umsResourceCategoryRequestDTO);
        return flag ? Result.success() : Result.fail("修改资源分类失败");
    }

    @Operation(summary = "根据ID删除后台资源")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable @NotNull Long id) {
        boolean flag = umsResourceCategoryService.delete(id);
        return flag ? Result.success() : Result.fail("删除资源分类失败");
    }
 }
