package org.zhuyuqinlan.lemall.business.admin.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Schema(name = "菜单响应dto")
public class UmsMenuResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "父节点id")
    private Long parentId;

    @Schema(description = "菜单级数")
    private Integer level;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "前端名称")
    private String name;

    @Schema(description = "前端图标")
    private String icon;

    @Schema(description = "前端隐藏")
    private Integer hidden;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "排序")
    private Integer sort;
}
