package org.zhuyuqinlan.lemall.business.portal.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "会员收藏管理", description = "MemberProductCollectionController")
@RequestMapping("${lemall.server.prefix.portal}/member/productCollection")
public class MemberProductCollectionController {
}
