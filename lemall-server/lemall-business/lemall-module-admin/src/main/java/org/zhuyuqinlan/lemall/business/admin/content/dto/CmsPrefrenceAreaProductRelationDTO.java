package org.zhuyuqinlan.lemall.business.admin.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "优选专区和商品关系响应DTO")
public class CmsPrefrenceAreaProductRelationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优选专区ID")
    private Long prefrenceAreaId;

    @Schema(description = "商品ID")
    private Long productId;
}
