package org.zhuyuqinlan.lemall.business.admin.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(description = "资源响应dto")
public class UmsResourceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源id")
    private Long id;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "资源url")
    private String url;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "资源分类id")
    private Long categoryId;

    @Schema(description = "创建时间")
    private Date updateTime;
}
