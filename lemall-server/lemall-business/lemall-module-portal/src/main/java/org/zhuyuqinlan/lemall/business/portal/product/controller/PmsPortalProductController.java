package org.zhuyuqinlan.lemall.business.portal.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;
import org.zhuyuqinlan.lemall.business.portal.product.dto.PmsPortalProductDetail;
import org.zhuyuqinlan.lemall.business.portal.product.dto.PmsProductCategoryNode;
import org.zhuyuqinlan.lemall.business.portal.product.service.PmsPortalProductService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "前台商品管理", description = "PmsPortalProductController")
@RequestMapping("${lemall.server.prefix.portal}/product/product")
public class PmsPortalProductController {

    private final PmsPortalProductService pmsPortalProductService;

    @Operation(summary = "综合搜索、筛选、排序")
    @Parameter(name = "sort", description = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低",
            in= ParameterIn.QUERY,schema = @Schema(type = "integer",defaultValue = "0",allowableValues = {"0","1","2","3","4"}))
    @GetMapping("/search")
    public Result<IPage<PmsProductDTO>> search(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Long brandId,
                                               @RequestParam(required = false) Long productCategoryId,
                                               @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                               @RequestParam(required = false, defaultValue = "0") Integer sort) {
        IPage<PmsProductDTO> productList = pmsPortalProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return Result.success(productList);
    }

    @Operation(summary = "以树形结构获取所有商品分类")
    @GetMapping("/categoryTreeList")
    public Result<List<PmsProductCategoryNode>> categoryTreeList() {
        List<PmsProductCategoryNode> list = pmsPortalProductService.categoryTreeList();
        return Result.success(list);
    }

    @Operation(summary = "获取前台商品详情")
    @GetMapping("/detail/{id}")
    public Result<PmsPortalProductDetail> detail(@PathVariable Long id) {
        PmsPortalProductDetail productDetail = pmsPortalProductService.detail(id);
        return Result.success(productDetail);
    }
}
