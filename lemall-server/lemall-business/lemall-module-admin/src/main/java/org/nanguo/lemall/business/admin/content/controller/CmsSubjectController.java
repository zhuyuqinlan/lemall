package org.nanguo.lemall.business.admin.content.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.content.dto.response.CmsSubjectResponseDTO;
import org.nanguo.lemall.business.admin.content.service.CmsSubjectService;
import org.nanguo.lemall.common.util.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/content/subject")
@Tag(name = "商品专题管理", description = "CmsSubjectController")
public class CmsSubjectController {

    private final CmsSubjectService cmsSubjectService;

    @Operation(summary = "获取全部商品专题")
    @GetMapping("/listAll")
    public Result<List<CmsSubjectResponseDTO>> listAll() {
        List<CmsSubjectResponseDTO> res = cmsSubjectService.listAll();
        return Result.success(res);
    }

    @Operation(summary = "根据专题名称分页获取专题")
    @GetMapping("/list")
    public Result<IPage<CmsSubjectResponseDTO>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
            IPage<CmsSubjectResponseDTO> res = cmsSubjectService.listPage(keyword, pageNum, pageSize);
            return Result.success(res);
    }

}
