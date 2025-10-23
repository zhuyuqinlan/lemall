package org.zhuyuqinlan.lemall.business.admin.sale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeAdvertiseRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeAdvertiseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeAdvertiseService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/sale/home/advertise")
@Tag(name = "首页轮播广告管理", description = "SmsHomeAdvertiseController")
public class SmsHomeAdvertiseController {

    private final SmsHomeAdvertiseService advertiseService;

    @Operation(summary = "添加广告")
    @PostMapping("/create")
    public Result<?> create(@RequestBody SmsHomeAdvertiseRequestDTO advertise) {
        boolean b = advertiseService.create(advertise);
        return b ? Result.success() : Result.fail("添加广告失败");
    }

    @Operation(summary = "删除广告")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam("ids") List<Long> ids) {
        boolean b = advertiseService.delete(ids);
        return b ? Result.success() : Result.fail("删除广告失败");
    }

    @Operation(summary = "修改上下线状态")
    @PostMapping("/update/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, Integer status) {
        boolean b = advertiseService.updateStatus(id, status);
        return b ? Result.success() : Result.fail("修改上下线状态失败");
    }

    @Operation(summary = "获取广告详情")
    @GetMapping("/{id}")
    public Result<SmsHomeAdvertiseDTO> getItem(@PathVariable Long id) {
        SmsHomeAdvertiseDTO advertise = advertiseService.getItem(id);
        return Result.success(advertise);
    }

    @Operation(summary = "修改广告")
    @PostMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody SmsHomeAdvertiseRequestDTO advertise) {
        boolean b = advertiseService.updateHomeAdvertise(id, advertise);
        return b ? Result.success() : Result.fail("修改广告失败");
    }

    @Operation(summary = "分页查询广告")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SmsHomeAdvertiseDTO>> list(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "type", required = false) Integer type,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        IPage<SmsHomeAdvertiseDTO> advertiseList = advertiseService.listPage(name, type, endTime, pageSize, pageNum);
        return Result.success(advertiseList);
    }
}
