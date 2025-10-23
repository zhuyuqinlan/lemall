package org.zhuyuqinlan.lemall.business.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsMenuRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsMenuNodeDTO;
import org.zhuyuqinlan.lemall.common.dto.UmsMenuDTO;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsMenuService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/system/menu")
@Tag(name = "菜单管理", description = "UmsMenuController")
public class UmsMenuController {

    private final UmsMenuService umsMenuService;

    @Operation(summary = "树形结构返回所有菜单列表")
    @GetMapping("/treeList")
    public Result<List<UmsMenuNodeDTO>> treeList() {
        List<UmsMenuNodeDTO> umsMenuNodeResponseDTO = umsMenuService.treeList();
        return Result.success(umsMenuNodeResponseDTO);
    }

    @Operation(summary = "分页查询后台菜单")
    @GetMapping("/list/{parentId}")
    public Result<PageResult<UmsMenuDTO>> list(@PathVariable @NotNull Long parentId,
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<UmsMenuDTO> menuResponseDTOIPage = umsMenuService.pageMenu(parentId, pageNum, pageSize);
        return Result.success(PageResult.fromMybatis(menuResponseDTOIPage));
    }

    @Operation(summary = "添加后台菜单")
    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated UmsMenuRequestDTO umsMenuParamRequestDTO) {
        int count = umsMenuService.create(umsMenuParamRequestDTO);
        return count > 0 ? Result.success() : Result.fail("创建后台菜单失败");
    }


    @Operation(summary = "根据ID获取菜单详情")
    @GetMapping("/{id}")
    public Result<UmsMenuDTO> getById(@PathVariable @NotNull Long id) {
        UmsMenuDTO umsMenuResponseDTO = umsMenuService.getItem(id);
        return Result.success(umsMenuResponseDTO);
    }

    @Operation(summary = "修改后台菜单")
    @PostMapping("/update/{id}")
    public Result<?> update(@RequestBody @Validated UmsMenuRequestDTO umsMenuParamRequestDTO, @PathVariable @NotNull Long id) {
        boolean flag = umsMenuService.updateMenu(umsMenuParamRequestDTO,id);
        return flag ? Result.success() : Result.fail("修改后台菜单失败");
    }

    @Operation(summary = "修改菜单显示状态")
    @PostMapping("/updateHidden/{id}")
    public Result<?> updateHidden(@PathVariable @NotNull Long id, @RequestParam("hidden") @NotNull Integer hidden) {
        UmsMenuRequestDTO umsMenuParamRequestDTO = new UmsMenuRequestDTO();
        umsMenuParamRequestDTO.setHidden(hidden);
        boolean b = umsMenuService.updateMenu(umsMenuParamRequestDTO, id);
        return b ? Result.success() : Result.fail("修改菜单显示状态失败");
    }

    @Operation(summary = "根据ID删除后台菜单")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable @NotNull Long id) {
        boolean flag = umsMenuService.deleteMenu(id);
        return flag ? Result.success() : Result.fail("删除菜单失败");
    }
}
