package org.zhuyuqinlan.lemall.business.admin.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Schema(description = "树结构的菜单响应dto")
public class UmsMenuNodeDTO extends UmsMenuDTO {

    @Schema(description = "子菜单")
    private List<UmsMenuNodeDTO> children;
}
