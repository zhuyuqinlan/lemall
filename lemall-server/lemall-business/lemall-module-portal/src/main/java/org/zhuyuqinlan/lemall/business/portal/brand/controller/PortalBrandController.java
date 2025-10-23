package org.zhuyuqinlan.lemall.business.portal.brand.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.brand.service.PortalBrandService;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsBrandDTO;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "前台品牌管理", description = "PortalBrandController")
@RequestMapping("${lemall.server.prefix.portal}/brand/brand")
public class PortalBrandController {
    private final PortalBrandService portalBrandService;

    @Operation(summary = "分页获取推荐品牌")
    @GetMapping("/recommendList")
    public Result<List<PmsBrandDTO>> recommendList(@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsBrandDTO> brandList = portalBrandService.recommendList(pageNum, pageSize);
        return Result.success(brandList);
    }

    @Operation(summary = "获取品牌详情")
    @GetMapping("/detail/{brandId}")
    public Result<PmsBrandDTO> detail(@PathVariable Long brandId) {
        PmsBrandDTO brand = portalBrandService.detail(brandId);
        return Result.success(brand);
    }

    @Operation(summary = "分页获取品牌相关商品")
    @GetMapping("/productList")
    public Result<IPage<PmsBrandDTO>> productList(@RequestParam Long brandId,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        IPage<PmsBrandDTO> result = portalBrandService.productList(brandId,pageNum, pageSize);
        return Result.success(result);
    }
}
