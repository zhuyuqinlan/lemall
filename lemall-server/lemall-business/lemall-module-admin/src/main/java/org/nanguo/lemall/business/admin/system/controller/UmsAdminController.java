package org.nanguo.lemall.business.admin.system.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.system.dto.request.UmsAdminRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.request.UserAdminLoginRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsAdminResponseDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsRoleResponseDTO;
import org.nanguo.lemall.business.admin.system.service.UmsAdminService;
import org.nanguo.lemall.common.dto.AdminUserDto;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/system/admin")
@Tag(name = "用户管理",description = "AdminController")
public class UmsAdminController {

    private final UmsAdminService adminService;

    @Value("${sa-token.token-prefix}")
    private String tokenHead;

    @Operation(summary = "登录",description = "后台管理登录后返回token")
    @PostMapping("/login")
    public Result<?> login(@RequestBody @Validated UserAdminLoginRequestDTO loginRequestDTO) {
        SaTokenInfo saTokenInfo  = adminService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        if (saTokenInfo == null) {
            return Result.fail("登录失败");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", saTokenInfo.getTokenValue() );
        tokenMap.put("tokenHead", tokenHead+" ");
        return Result.success(tokenMap);
    }

    // 退出登录接口
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<String> logout() {
        // 调用sa-token的退出登录方法，只需一行代码搞定
        StpUtil.logout();
        return Result.success("成功退出登录");
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<AdminUserDto> getAdminInfo() {
        AdminUserDto admin = adminService.getCurrentAdmin();
        return Result.success(admin);
    }

    @Operation(summary = "根据用户名或姓名分页获取用户列表")
    @GetMapping("/list")
    public Result<IPage<UmsAdminResponseDTO>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<UmsAdminResponseDTO> adminList = adminService.listPage(keyword, pageSize, pageNum);
        return Result.success(adminList);
    }

    //TODO 添加用户时，允许上传头像
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UmsAdminResponseDTO> register(@RequestBody @Validated UmsAdminRequestDTO umsAdminParam) {
        UmsAdminResponseDTO umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            return Result.fail("注册失败");
        }
        return Result.success(umsAdmin);
    }

    @Operation(summary = "修改帐号状态")
    @PostMapping("/updateStatus/{id}")
    public Result<?> updateStatus(@PathVariable @NotNull Long id, @RequestParam(value = "status") @NotNull Integer status) {
        UmsAdminRequestDTO umsAdminRequestDTO = new UmsAdminRequestDTO();
        umsAdminRequestDTO.setStatus(status);
        boolean flag = adminService.updateAdmin(id, umsAdminRequestDTO);
        return flag ? Result.success() : Result.fail("修改账号状态失败");
    }

    @Operation(summary = "修改指定用户信息")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable @NotNull Long id, @Validated @RequestBody UmsAdminRequestDTO admin) {
        boolean flag = adminService.updateAdmin(id,admin);
        return flag ? Result.success() : Result.fail("修改账号信息失败");
    }

    @Operation(summary = "删除指定用户信息")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable @NotNull Long id) {
        boolean flag = adminService.deleteAdmin(id);
        return flag ? Result.success() : Result.fail("删除用户失败");
    }

    @Operation(summary = "获取指定用户的角色")
    @GetMapping("/role/{adminId}")
    public Result<List<UmsRoleResponseDTO>> getRoleList(@PathVariable @NotNull Long adminId) {
        List<UmsRoleResponseDTO> roleList = adminService.getRoleList(adminId);
        return Result.success(roleList);
    }

    @Operation(summary = "给用户分配角色")
    @PostMapping("/role/update")
    public Result<?> updateRole(@RequestParam("adminId") @NotNull Long adminId, @RequestParam("roleIds") @NotEmpty List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        return count > 0 ? Result.success() : Result.fail("给用户分配角色失败");
    }
}
