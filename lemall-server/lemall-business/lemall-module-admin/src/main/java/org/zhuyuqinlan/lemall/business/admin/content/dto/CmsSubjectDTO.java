package org.zhuyuqinlan.lemall.business.admin.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Schema(description = "专题响应DTO")
public class CmsSubjectDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "专题标题")
    private String title;

    @Schema(description = "专题主图")
    private String pic;

    @Schema(description = "关联产品数量")
    private Integer productCount;

    @Schema(description = "推荐状态")
    private Integer recommendStatus;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "收藏数")
    private Integer collectCount;

    @Schema(description = "阅读数")
    private Integer readCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "画册图片，用逗号分割")
    private String albumPics;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "显示状态：0->不显示；1->显示")
    private Integer showStatus;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "转发数")
    private Integer forwardCount;

    @Schema(description = "专题分类名称")
    private String categoryName;
}

