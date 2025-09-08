package org.nanguo.lemall.business.admin.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.product.dto.request.PmsBrandRequestDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsBrandResponseDTO;
import org.nanguo.lemall.business.admin.product.service.PmsBrandService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/product/brand")
@Tag(name = "品牌管理",description = "PmsBrandController")
public class PmsBrandController {

    private final PmsBrandService brandService;

    @Operation(summary = "根据品牌名称分页获取品牌列表")
    @GetMapping("/list")
    public Result<IPage<PmsBrandResponseDTO>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        IPage<PmsBrandResponseDTO> res =  brandService.getList(keyword,pageNum,pageSize);
        return Result.success(res);
    }

    @Operation(summary = "获取全部品牌列表")
    @GetMapping("/listAll")
    public Result<List<PmsBrandResponseDTO>> getListAll() {
        List<PmsBrandResponseDTO> brandResponseDTOS = brandService.getListAll();
        return Result.success(brandResponseDTOS);
    }

    @Operation(summary = "添加品牌")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody PmsBrandRequestDTO requestDTO) {
        boolean b = brandService.create(requestDTO);
        return b ? Result.success() : Result.fail("添加品牌失败");
    }

    @Operation(summary = "更新品牌")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable("id") Long id, @Validated @RequestBody PmsBrandRequestDTO requestDTO) {
        boolean b = brandService.updateBrand(id,requestDTO);
        return b ? Result.success() : Result.fail("更新品牌失败");
    }

    @Operation(summary = "删除品牌")
    @GetMapping(value = "/delete/{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        boolean b = brandService.deleteBrandById(id);
        return b ? Result.success() : Result.fail("删除品牌失败");
    }

    @Operation(summary = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public Result<PmsBrandResponseDTO> getItem(@PathVariable("id") Long id) {
        PmsBrandResponseDTO pmsBrandResponseDTO = brandService.getBrandById(id);
        return Result.success(pmsBrandResponseDTO);
    }

    @Operation(summary = "批量删除品牌")
    @PostMapping("/delete/batch")
    public Result<?> deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean b = brandService.deleteBrandBatch(ids);
        return b ? Result.success() : Result.fail("批量删除品牌失败");
    }

    @Operation(summary = "批量更新显示状态")
    @PostMapping("/update/showStatus")
    public Result<?> updateShowStatus(@RequestParam("ids") List<Long> ids,
                                     @RequestParam("showStatus") Integer showStatus) {
        boolean b = brandService.updateBrandByIds(ids,showStatus);
        return b ? Result.success() : Result.fail("批量更新显示状态失败");
    }

    @Operation(summary = "批量更新厂家制造商状态")
    @PostMapping("/update/factoryStatus")
    public Result<?> updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                         @RequestParam("factoryStatus") Integer factoryStatus) {
        boolean b = brandService.updateFactoryStatus(ids,factoryStatus);
        return b ? Result.success() : Result.fail("批量更新厂家制造商状态失败");
    }
}
