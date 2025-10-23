package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberProductCollectionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.MemberProductCollectionDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.MemberCollectionService;
import org.zhuyuqinlan.lemall.common.response.Result;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员收藏管理", description = "MemberProductCollectionController")
@RequestMapping("${lemall.server.prefix.portal}/member/productCollection")
public class MemberProductCollectionController {

    private final MemberCollectionService memberCollectionService;

    @Operation(summary = "添加商品收藏")
    @PostMapping("/add")
    public Result<?> add(@RequestBody MemberProductCollectionRequestDTO productCollection) {
        int count = memberCollectionService.add(productCollection);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail("添加商品收藏失败");
        }
    }

    @Operation(summary = "删除收藏商品")
    @PostMapping("/delete")
    public Result<?> delete(Long productId) {
        int count = memberCollectionService.delete(productId);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail("删除收藏商品失败");
        }
    }

    @Operation(summary = "显示收藏列表")
    @GetMapping("/list")
    public Result<Page<MemberProductCollectionDTO>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<MemberProductCollectionDTO> page = memberCollectionService.list(pageNum,pageSize);
        return Result.success(page);
    }

    @Operation(summary = "显示收藏商品详情")
    @GetMapping("/detail")
    public Result<MemberProductCollectionDTO> detail(@RequestParam Long productId) {
        MemberProductCollectionDTO memberProductCollection = memberCollectionService.detail(productId);
        return Result.success(memberProductCollection);
    }

    @Operation(summary = "清空收藏列表")
    @PostMapping("/clear")
    public Result<?> clear() {
        memberCollectionService.clear();
        return Result.success(null);
    }
}
