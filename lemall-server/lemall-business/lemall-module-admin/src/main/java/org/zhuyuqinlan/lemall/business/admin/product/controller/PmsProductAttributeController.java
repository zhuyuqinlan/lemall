package org.zhuyuqinlan.lemall.business.admin.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.business.admin.product.dto.PmsProductAttributeDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.ProductAttrInfo;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductAttributeParam;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductAttributeService;
import org.zhuyuqinlan.lemall.common.response.PageResult;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/product/productAttribute")
@Tag(name = "商品属性管理",description = "PmsProductAttributeController")
public class PmsProductAttributeController {
    private final PmsProductAttributeService pmsProductAttributeService;

    @Operation(summary = "根据分类查询属性列表或参数列表")
    @Parameters({@Parameter(name = "type", description = "0表示属性，1表示参数", required = true,in = ParameterIn.QUERY, schema = @Schema(type = "integer"))})
    @GetMapping("/list/{cid}")
    public Result<PageResult<PmsProductAttributeDTO>> getList(@PathVariable Long cid,
                                                              @RequestParam(value = "type") Integer type,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<PmsProductAttributeDTO> productAttributeDTOIPage =  pmsProductAttributeService.getList(cid, type, pageSize, pageNum);
        return Result.success(PageResult.fromMybatis(productAttributeDTOIPage));
    }


    @Operation(summary = "添加商品属性信息")
    @PostMapping("/create")
    public Result<?> create(@RequestBody PmsProductAttributeParam productAttributeParam) {
        boolean flag = pmsProductAttributeService.create(productAttributeParam);
        return flag ? Result.success() : Result.fail("添加商品属性信息失败");
    }

    @Operation(summary = "修改商品属性信息")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody PmsProductAttributeParam productAttributeParam) {
        boolean flag = pmsProductAttributeService.updateAttr(id,productAttributeParam);
        return flag ? Result.success() : Result.fail("修改商品属性信息失败");
    }

    @Operation(summary = "查询单个商品属性")
    @GetMapping("/{id}")
    public Result<PmsProductAttributeDTO> getItem(@PathVariable Long id) {
        PmsProductAttributeDTO pmsProductAttributeDTO = pmsProductAttributeService.getItem(id);
        return Result.success(pmsProductAttributeDTO);
    }

    @Operation(summary = "批量删除商品属性")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        int flag = pmsProductAttributeService.delete(ids);
        return flag > 0 ? Result.success() : Result.fail("批量删除商品属性失败");
    }

    @Operation(summary = "根据商品分类的id获取商品属性及属性分类")
    @GetMapping("/attrInfo/{productCategoryId}")
    public Result<List<ProductAttrInfo>> getAttrInfo(@PathVariable Long productCategoryId) {
        List<ProductAttrInfo> productAttrInfoList = pmsProductAttributeService.getProductAttrInfo(productCategoryId);
        return Result.success(productAttrInfoList);
    }
}
