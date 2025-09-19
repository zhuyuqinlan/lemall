package org.zhuyuqinlan.lemall.business.admin.system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(name = "资源分类响应dto")
public class UmsResourceCategoryResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;
}
