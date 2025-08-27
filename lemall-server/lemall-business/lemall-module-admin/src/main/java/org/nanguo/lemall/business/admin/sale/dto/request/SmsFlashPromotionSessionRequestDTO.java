package org.nanguo.lemall.business.admin.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Schema(name = "限时购场次响应DTO")
public class SmsFlashPromotionSessionRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "场次名称")
    private String name;

    @Schema(description = "每日开始时间")
    private LocalTime startTime;

    @Schema(description = "每日结束时间")
    private LocalTime endTime;

    @Schema(description = "启用状态：0->不启用；1->启用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

