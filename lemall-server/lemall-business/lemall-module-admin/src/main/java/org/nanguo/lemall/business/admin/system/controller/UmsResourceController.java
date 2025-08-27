package org.nanguo.lemall.business.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.system.dto.request.UmsResourceRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsResourceResponseDTO;
import org.nanguo.lemall.business.admin.system.service.UmsResourceService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/lemall-admin/system/resource")
@Tag(name = "资源管理")
public class UmsResourceController {

    private final UmsResourceService umsResourceService;

    @Operation(summary = "查询所有后台资源")
    @GetMapping("/listAll")
    public Result<List<UmsResourceResponseDTO>> listAll() {
        List<UmsResourceResponseDTO> umsResourceResponseDTOS = umsResourceService.listAll();
        return Result.success(umsResourceResponseDTOS);
    }

    @Operation(summary = "分页模糊查询后台资源")
    @GetMapping("/list")
    public Result<IPage<UmsResourceResponseDTO>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<UmsResourceResponseDTO> umsResourceResponseDTOIPage = umsResourceService.pageRes(categoryId, nameKeyword, urlKeyword, pageNum, pageSize);
        return Result.success(umsResourceResponseDTOIPage);
    }

    @Operation(summary = "添加后台资源")
    @PostMapping("/create")
    public Result<?> create(@Validated @RequestBody UmsResourceRequestDTO umsResource) {
        boolean flag = umsResourceService.create(umsResource);
        return flag ? Result.success() : Result.fail("添加资源失败");
    }

    @Operation(summary = "修改后台资源")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable @NotNull Long id,
                            @Validated @RequestBody UmsResourceRequestDTO umsResourceRequestDTO) {
        boolean flag = umsResourceService.updateRes(id, umsResourceRequestDTO);
        return flag ? Result.success() : Result.fail("修改资源失败");
    }

    @Operation(summary = "根据ID删除后台资源")
    @PostMapping("/delete/{id}")
    public Result<?> delete(@PathVariable @NotNull Long id) {
        boolean flag = umsResourceService.deleteRes(id);
        return flag ? Result.success() : Result.fail("删除资源失败");
    }

}
