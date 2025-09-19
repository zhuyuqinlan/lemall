package org.zhuyuqinlan.lemall.business.admin.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsCompanyAddressResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsCompanyAddressService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${lemall.server.prefix.admin}/order/companyAddress")
@Tag(name = "收货地址管理", description = "OmsCompanyAddressController")
public class OmsCompanyAddressController {
    private final OmsCompanyAddressService companyAddressService;

    @Operation(summary = "获取所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<OmsCompanyAddressResponseDTO>> list() {
        List<OmsCompanyAddressResponseDTO> companyAddressList = companyAddressService.listAll();
        return Result.success(companyAddressList);
    }
}
