package org.zhuyuqinlan.lemall.business.admin.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhuyuqinlan.lemall.business.admin.product.dto.UmsMemberLevelDTO;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsMemberLevelService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/system/memberLevel")
@Tag(name = "商品属性管理",description = "UmsMemberLevelController")
public class UmsMemberLevelController {

    private final UmsMemberLevelService umsMemberLevelService;

    @Operation(summary = "查询所有会员等级")
    @GetMapping("/list")
    public Result<List<UmsMemberLevelDTO>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevelDTO> memberLevelList = umsMemberLevelService.listLevel(defaultStatus);
        return Result.success(memberLevelList);
    }
}
