package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员商品浏览记录管理", description = "MemberReadHistoryController")
@RequestMapping("${lemall.server.prefix.portal}/member/readHistory")
public class MemberReadHistoryController {
}
