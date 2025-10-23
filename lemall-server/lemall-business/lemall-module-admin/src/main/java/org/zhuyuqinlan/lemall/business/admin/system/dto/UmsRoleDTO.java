package org.zhuyuqinlan.lemall.business.admin.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(description = "角色响应dto")
public class UmsRoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色id")
    private Long id;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "启用状态：0->禁用；1->启用")
    private Integer status;

    @Schema(description = "后台用户数量")
    private Integer adminCount;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "添加时间")
    private Date createTime;
}
