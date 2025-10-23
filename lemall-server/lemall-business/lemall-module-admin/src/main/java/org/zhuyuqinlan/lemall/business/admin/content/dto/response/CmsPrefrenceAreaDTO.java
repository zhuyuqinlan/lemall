package org.zhuyuqinlan.lemall.business.admin.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "优选专区响应DTO")
public class CmsPrefrenceAreaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "专区名称")
    private String name;

    @Schema(description = "副标题")
    private String subTitle;

    @Schema(description = "展示图片")
    private byte[] pic;  // 数据库存 varbinary，可以用 byte[] 接收，或者转成 Base64 给前端

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "显示状态：0->不显示；1->显示")
    private Integer showStatus;
}

