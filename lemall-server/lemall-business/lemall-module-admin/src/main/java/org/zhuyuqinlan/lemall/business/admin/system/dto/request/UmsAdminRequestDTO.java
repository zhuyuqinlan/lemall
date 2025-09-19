package org.zhuyuqinlan.lemall.business.admin.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "用户请求类")
public class UmsAdminRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户头像")
    private String icon;

    @Schema(description = "邮箱")
    @Email
    private String email;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "用户状态")
    @NotNull
    private Integer status;
}
