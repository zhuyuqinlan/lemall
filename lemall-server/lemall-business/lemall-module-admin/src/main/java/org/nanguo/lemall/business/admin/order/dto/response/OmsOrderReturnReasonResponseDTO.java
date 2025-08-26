package org.nanguo.lemall.business.admin.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "退货原因响应DTO")
public class OmsOrderReturnReasonResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "退货类型")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0->不启用；1->启用")
    private Integer status;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;
}

