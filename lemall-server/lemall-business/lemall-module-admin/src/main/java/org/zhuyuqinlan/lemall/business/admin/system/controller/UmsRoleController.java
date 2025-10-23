package org.zhuyuqinlan.lemall.business.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsRoleRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsMenuDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsResourceDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsRoleDTO;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsRoleService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/system/role")
@Tag(name = "角色管理",description = "UmsRoleController")
public class UmsRoleController {

    private final UmsRoleService umsRoleService;

    @Operation(summary = "获取所有角色")
    @GetMapping("/listAll")
    public Result<List<UmsRoleDTO>> listAll() {
        List<UmsRoleDTO> roleList = umsRoleService.listAll();
        return Result.success(roleList);
    }

    @Operation(summary = "根据角色名称分页获取角色列表")
    @GetMapping("list")
    public Result<PageResult<UmsRoleDTO>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<UmsRoleDTO> umsRoleIPage = umsRoleService.pageRole(keyword,pageSize,pageNum);
        return Result.success(PageResult.fromMybatis(umsRoleIPage));
    }

    @Operation(summary = "修改角色状态")
    @PostMapping("/updateStatus/{id}")
    public Result<?> updateStatus(@PathVariable @NotNull Long id, @RequestParam(value = "status") @NotNull Integer status) {
        UmsRoleRequestDTO umsRole = new UmsRoleRequestDTO();
        umsRole.setStatus(status);
        int flag = umsRoleService.updateRole(id,umsRole);
        return flag > 0 ? Result.success() : Result.fail("修改角色状态失败");
    }

    @Operation(summary = "修改角色")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable @NotNull Long id, @RequestBody @Validated UmsRoleRequestDTO role) {
        int flag = umsRoleService.updateRole(id,role);
        return flag > 0 ? Result.success() : Result.fail("修改角色失败");
    }

    @Operation(summary = "批量删除角色")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") @NotEmpty List<Long> ids) {
        boolean flag = umsRoleService.deleteRoles(ids);
        return flag ? Result.success() : Result.fail("删除角色失败");
    }

    @Operation(summary = "添加角色")
    @PostMapping("/create")
    public Result<?> create(@RequestBody @Validated UmsRoleRequestDTO role) {
        boolean flag = umsRoleService.create(role);
        return flag ? Result.success() : Result.fail("添加角色失败");
    }

    @Operation(summary = "获取角色相关菜单")
    @GetMapping("/listMenu/{roleId}")
    public Result<List<UmsMenuDTO>> listMenu(@PathVariable @NotNull Long roleId) {
        List<UmsMenuDTO> umsMenuNodeResponseDTOS = umsRoleService.listMenu(roleId);
        return Result.success(umsMenuNodeResponseDTOS);
    }

    @Operation(summary = "给角色分配菜单")
    @PostMapping("/allocMenu")
    public Result<?> allocMenu(@RequestParam @NotNull Long roleId, @RequestParam @NotEmpty List<Long> menuIds) {
        int count = umsRoleService.allocMenu(roleId, menuIds);
        return Result.success(count);
    }

    @Operation(summary = "获取角色相关资源")
    @GetMapping("/listResource/{roleId}")
    public Result<List<UmsResourceDTO>> getResourceList(@PathVariable @NotNull Long roleId) {
        List<UmsResourceDTO> umsResourceResponseDTOS = umsRoleService.listResource(roleId);
        return Result.success(umsResourceResponseDTOS);
    }

    @Operation(summary = "给角色分配资源")
    @PostMapping("/allocResource")
    public Result<?> allocResource(@RequestParam @NotNull Long roleId, @RequestParam @NotEmpty List<Long> resourceIds) {
        int count = umsRoleService.allocResource(roleId, resourceIds);
        return Result.success(count);
    }
}
