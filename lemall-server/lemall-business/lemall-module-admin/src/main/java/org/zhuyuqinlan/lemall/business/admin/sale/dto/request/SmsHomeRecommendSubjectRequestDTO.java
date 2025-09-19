package org.zhuyuqinlan.lemall.business.admin.sale.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "首页推荐专题响应DTO")
public class SmsHomeRecommendSubjectRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "专题ID")
    private Long subjectId;

    @Schema(description = "专题名称")
    private String subjectName;

    @Schema(description = "推荐状态：0->不推荐；1->推荐")
    private Integer recommendStatus;

    @Schema(description = "排序")
    private Integer sort;
}
