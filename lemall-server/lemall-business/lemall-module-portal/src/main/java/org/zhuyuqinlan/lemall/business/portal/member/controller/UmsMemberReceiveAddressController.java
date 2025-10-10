package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.UmsMemberReceiveAddressRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.UmsMemberReceiveAddressResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberReceiveAddressService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员收货地址管理", description = "UmsMemberReceiveAddressController")
@RequestMapping("${lemall.server.prefix.portal}/member/address")
public class UmsMemberReceiveAddressController {

    private final UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @Operation(summary = "添加收货地址")
    @PostMapping("/add")
    public Result<?> add(@RequestBody UmsMemberReceiveAddressRequestDTO address) {
        boolean flag = umsMemberReceiveAddressService.add(address);
        return flag ? Result.success() : Result.fail("添加收获地址失败");
    }

    @Operation(summary = "删除收货地址")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        boolean flag = umsMemberReceiveAddressService.delete(id);
        return flag ? Result.success() : Result.fail("删除收获地址失败");
    }

    @Operation(summary = "修改收货地址")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddressRequestDTO address) {
        boolean flag = umsMemberReceiveAddressService.updateAddress(id,address);
        return flag ? Result.success() : Result.fail("修改收货地址失败");
    }

    @Operation(summary = "显示所有收货地址")
    @GetMapping("/list")
    public Result<List<UmsMemberReceiveAddressResponseDTO>> list() {
        List<UmsMemberReceiveAddressResponseDTO> res = umsMemberReceiveAddressService.listAddress();
        return Result.success(res);
    }

    @Operation(summary = "获取收货地址详情")
    @GetMapping("/{id}")
    public Result<UmsMemberReceiveAddressResponseDTO> get(@PathVariable Long id) {
        UmsMemberReceiveAddressResponseDTO addressResponseDTO = umsMemberReceiveAddressService.getItem(id);
        return Result.success(addressResponseDTO);
    }
}
