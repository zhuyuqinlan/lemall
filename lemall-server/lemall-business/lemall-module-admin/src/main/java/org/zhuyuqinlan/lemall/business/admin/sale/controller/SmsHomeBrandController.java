package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeBrandRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeBrandDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeBrandService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/home/brand")
@Tag(name = "首页品牌管理", description = "SmsHomeBrandController")
public class SmsHomeBrandController {

    private final SmsHomeBrandService homeBrandService;

    @Operation(summary = "添加首页推荐品牌")
    @PostMapping("/create")
    public Result<?> create(@RequestBody List<SmsHomeBrandRequestDTO> homeBrandList) {
        boolean b = homeBrandService.create(homeBrandList);
        return b ? Result.success() : Result.fail("添加首页推荐品牌失败");
    }

    @Operation(summary = "修改品牌排序")
    @PostMapping("/update/sort/{id}")
    public Result<?> updateSort(@PathVariable Long id, Integer sort) {
        boolean b = homeBrandService.updateSort(id, sort);
        return b ? Result.success() : Result.fail("修改品牌排序失败");
    }

    @Operation(summary = "批量删除推荐品牌")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = homeBrandService.delete(ids);
        return b ? Result.success() : Result.fail("批量删除推荐品牌失败");
    }

    @Operation(summary = "批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Result<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        boolean b = homeBrandService.updateRecommendStatus(ids, recommendStatus);
        return b ? Result.success() : Result.fail("批量删除推荐品牌失败");
    }

    @Operation(summary = "分页查询推荐品牌")
    @RequestMapping(value = "/list", method = RequestMethod.GET)

    public Result<PageResult<SmsHomeBrandDTO>> list(@RequestParam(value = "brandName", required = false) String brandName,
                                                    @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsHomeBrandDTO> homeBrandList = homeBrandService.listPage(brandName, recommendStatus, pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(homeBrandList));
    }
}
