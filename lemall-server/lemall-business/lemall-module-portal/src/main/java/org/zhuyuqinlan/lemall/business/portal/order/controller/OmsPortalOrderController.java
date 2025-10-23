package org.zhuyuqinlan.lemall.business.portal.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "订单管理",description = "OmsPortalOrderController")
@RequestMapping("${lemall.server.prefix.portal}/order")
public class OmsPortalOrderController {

}
