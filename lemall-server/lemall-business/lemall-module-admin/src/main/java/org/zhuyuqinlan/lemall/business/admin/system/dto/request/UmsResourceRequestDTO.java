package org.zhuyuqinlan.lemall.business.admin.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "资源请求dto")
public class UmsResourceRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "资源名称")
    private String name;

    @NotBlank
    @Schema(description = "资源url")
    private String url;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "资源分类id")
    private Long categoryId;
}
