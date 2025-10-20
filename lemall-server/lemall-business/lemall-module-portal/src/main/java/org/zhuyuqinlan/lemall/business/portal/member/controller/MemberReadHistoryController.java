package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberReadHistoryRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.MemberReadHistoryResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.service.MemberReadHistoryService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员商品浏览记录管理", description = "MemberReadHistoryController")
@RequestMapping("${lemall.server.prefix.portal}/member/readHistory")
public class MemberReadHistoryController {

    private final MemberReadHistoryService memberReadHistoryService;

    @Operation(summary = "创建浏览记录")
    @PostMapping("/create")
    public Result<?> create(@RequestBody MemberReadHistoryRequestDTO memberReadHistory) {
        int count = memberReadHistoryService.create(memberReadHistory);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail("创建浏览记录失败");
        }
    }

    @Operation(summary = "删除浏览记录")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<String> ids) {
        int count = memberReadHistoryService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail("删除浏览记录失败");
        }
    }

    @Operation(summary = "清空除浏览记录")
    @PostMapping("/clear")
    public Result<?> clear() {
        memberReadHistoryService.clear();
        return Result.success();
    }

    @Operation(summary = "分页获取用户浏览记录")
    @GetMapping("/list")
    public Result<Page<MemberReadHistoryResponseDTO>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<MemberReadHistoryResponseDTO> page = memberReadHistoryService.list(pageNum, pageSize);
        return Result.success(page);
    }
}
