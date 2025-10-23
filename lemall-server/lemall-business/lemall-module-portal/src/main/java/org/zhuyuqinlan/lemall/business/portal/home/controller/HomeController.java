package org.zhuyuqinlan.lemall.business.portal.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.home.dto.CmsSubjectDTO;
import org.zhuyuqinlan.lemall.business.portal.home.dto.HomeContentResult;
import org.zhuyuqinlan.lemall.business.portal.home.dto.PmsProductCategoryDTO;
import org.zhuyuqinlan.lemall.business.portal.home.service.HomeService;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "首页内容管理", description = "HomeController")
@RequestMapping("${lemall.server.prefix.portal}/home/home")
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "首页内容页信息展示")
    @GetMapping("/content")
    public Result<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return Result.success(contentResult);
    }

    @Operation(summary = "分页获取推荐商品")
    @GetMapping("/recommendProductList")
    public Result<List<PmsProductDTO>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductDTO> productList = homeService.recommendProductList(pageSize, pageNum);
        return Result.success(productList);
    }

    @Operation(summary = "获取首页商品分类")
    @GetMapping("/productCateList/{parentId}")
    public Result<List<PmsProductCategoryDTO>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategoryDTO> productCategoryList = homeService.getProductCateList(parentId);
        return Result.success(productCategoryList);
    }

    @Operation(summary = "根据分类获取专题")
    @GetMapping("/subjectList")
    public Result<List<CmsSubjectDTO>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubjectDTO> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
        return Result.success(subjectList);
    }

    @Operation(summary = "分页获取人气推荐商品")
    @GetMapping("/hotProductList")
    public Result<List<PmsProductDTO>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProductDTO> productList = homeService.hotProductList(pageNum,pageSize);
        return Result.success(productList);
    }

    @Operation(summary = "分页获取新品推荐商品")
    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<PmsProductDTO>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProductDTO> productList = homeService.newProductList(pageNum,pageSize);
        return Result.success(productList);
    }
}
