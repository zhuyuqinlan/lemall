package org.zhuyuqinlan.lemall.business.admin.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Schema(name = "树结构的菜单响应dto")
public class UmsMenuNodeResponseDTO extends UmsMenuResponseDTO {

    @Schema(description = "子菜单")
    private List<UmsMenuNodeResponseDTO> children;
}
