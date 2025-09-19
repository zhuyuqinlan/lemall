package org.zhuyuqinlan.lemall.business.admin.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "角色请求类")
public class UmsRoleRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @NotNull
    @Schema(description = "状态")
    private Integer status;
}
