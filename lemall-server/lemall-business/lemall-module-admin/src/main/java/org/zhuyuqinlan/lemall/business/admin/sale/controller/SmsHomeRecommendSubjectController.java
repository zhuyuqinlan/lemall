package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeRecommendSubjectRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeRecommendSubjectDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeRecommendSubjectService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/home/recommendSubject")
@Tag(name = "首页专题推荐管理", description = "SmsHomeRecommendSubjectController")
public class SmsHomeRecommendSubjectController {
    private final SmsHomeRecommendSubjectService recommendSubjectService;

    @Operation(summary = "添加首页推荐专题")
    @PostMapping("/create")
    public Result<?> create(@RequestBody List<SmsHomeRecommendSubjectRequestDTO> homeBrandList) {
        boolean b = recommendSubjectService.create(homeBrandList);
        return b ? Result.success() : Result.fail("添加首页推荐失败");
    }

    @Operation(summary = "修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Result<?> updateSort(@PathVariable Long id, Integer sort) {
        boolean b = recommendSubjectService.updateSort(id, sort);
        return b ? Result.success() : Result.fail("修改推荐排序失败");
    }

    @Operation(summary = "批量删除推荐")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = recommendSubjectService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除推荐失败");
    }

    @Operation(summary = "批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Result<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        boolean b = recommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        return b ? Result.success() : Result.fail("批量修改推荐状态失败");
    }

    @Operation(summary = "分页查询推荐")
    @GetMapping("/list")
    public Result<PageResult<SmsHomeRecommendSubjectDTO>> list(@RequestParam(value = "subjectName", required = false) String subjectName,
                                                               @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsHomeRecommendSubjectDTO> homeBrandList = recommendSubjectService.listPage(subjectName, recommendStatus, pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(homeBrandList));
    }
}
