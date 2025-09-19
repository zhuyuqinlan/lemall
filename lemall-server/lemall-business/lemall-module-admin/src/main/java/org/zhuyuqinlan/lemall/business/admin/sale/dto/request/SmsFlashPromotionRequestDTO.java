package org.zhuyuqinlan.lemall.business.admin.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "限时购活动响应DTO")
public class SmsFlashPromotionRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "秒杀时间段名称")
    private String title;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "上下线状态")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

