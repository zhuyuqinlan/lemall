package org.zhuyuqinlan.lemall.business.admin.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "优选专区和商品关系请求DTO")
public class CmsPrefrenceAreaProductRelationRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @Schema(description = "优选专区ID")
    private Long prefrenceAreaId;

    @NotNull
    @Schema(description = "商品ID")
    private Long productId;
}
