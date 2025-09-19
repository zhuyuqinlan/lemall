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
@Schema(name = "菜单请求dto")
public class UmsMenuRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "父级id")
    private Long parentId;

    @NotBlank
    @Schema(description = "菜单名称")
    private String title;

    @NotBlank
    @Schema(description = "前端名称")
    private String name;

    @NotBlank
    @Schema(description = "前端图标")
    private String icon;

    @NotNull
    @Schema(description = "排序")
    private Integer sort;

    @NotNull
    @Schema(description = "是否显示")
    private Integer hidden;
}
