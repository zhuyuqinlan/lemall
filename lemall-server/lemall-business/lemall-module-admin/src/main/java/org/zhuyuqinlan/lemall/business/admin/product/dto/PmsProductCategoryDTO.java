package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "产品分类响应DTO")
public class PmsProductCategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "上级分类的编号：0表示一级分类")
    private Long parentId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类级别：0->1级；1->2级")
    private Integer level;

    @Schema(description = "商品数量")
    private Integer productCount;

    @Schema(description = "商品单位")
    private String productUnit;

    @Schema(description = "是否显示在导航栏：0->不显示；1->显示")
    private Integer navStatus;

    @Schema(description = "显示状态：0->不显示；1->显示")
    private Integer showStatus;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "描述")
    private String description;
}
