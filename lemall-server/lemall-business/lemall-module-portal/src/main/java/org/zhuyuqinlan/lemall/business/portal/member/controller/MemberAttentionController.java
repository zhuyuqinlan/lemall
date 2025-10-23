package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberBrandAttentionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.MemberBrandAttentionDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.MemberAttentionService;
import org.zhuyuqinlan.lemall.common.response.Result;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员关注品牌管理", description = "MemberAttentionController")
@RequestMapping("${lemall.server.prefix.portal}/member/attention")
public class MemberAttentionController {

    private final MemberAttentionService memberAttentionService;

    @Operation(summary = "添加品牌关注")
    @PostMapping("/add")
    public Result<?> add(@RequestBody MemberBrandAttentionRequestDTO memberBrandAttention) {
        int count = memberAttentionService.add(memberBrandAttention);
        if(count>0){
            return Result.success(count);
        }else{
            return Result.fail("添加品牌关注失败");
        }
    }

    @Operation(summary = "取消关注")
    @PostMapping("/delete")
    public Result<?> delete(Long brandId) {
        int count = memberAttentionService.delete(brandId);
        if(count>0){
            return Result.success(count);
        }else{
            return Result.fail("取消关注失败");
        }
    }

    @Operation(summary = "显示关注列表")
    @GetMapping("/list")
    public Result<Page<MemberBrandAttentionDTO>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<MemberBrandAttentionDTO> page = memberAttentionService.list(pageNum,pageSize);
        return Result.success(page);
    }

    @Operation(summary = "显示关注品牌详情")
    @GetMapping("/detail")
    public Result<MemberBrandAttentionDTO> detail(@RequestParam Long brandId) {
        MemberBrandAttentionDTO memberBrandAttention = memberAttentionService.detail(brandId);
        return Result.success(memberBrandAttention);
    }

    @Operation(summary = "清空关注列表")
    @PostMapping("/clear")
    public Result<?> clear() {
        memberAttentionService.clear();
        return Result.success();
    }
}
