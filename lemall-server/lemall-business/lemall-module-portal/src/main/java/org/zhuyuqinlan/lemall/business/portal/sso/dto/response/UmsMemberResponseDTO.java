package org.zhuyuqinlan.lemall.business.portal.sso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "会员响应DTO")
public class UmsMemberResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "会员等级ID")
    private Long memberLevelId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "帐号启用状态:0->禁用；1->启用")
    private Integer status;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "头像")
    private String icon;

    @Schema(description = "性别：0->未知；1->男；2->女")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "职业")
    private String job;

    @Schema(description = "个性签名")
    private String personalizedSignature;

    @Schema(description = "用户来源")
    private Integer sourceType;

    @Schema(description = "积分")
    private Integer integration;

    @Schema(description = "成长值")
    private Integer growth;

    @Schema(description = "剩余抽奖次数")
    private Integer luckeyCount;

    @Schema(description = "历史积分数量")
    private Integer historyIntegration;
}

