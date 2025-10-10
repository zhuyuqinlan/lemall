package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员关注品牌管理", description = "会员关注品牌管理")
@RequestMapping("${lemall.server.prefix.portal}/member/attention")
public class MemberAttentionController {
}
