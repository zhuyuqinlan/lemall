package org.zhuyuqinlan.lemall.business.admin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "商品属性参数DTO")
public class PmsProductAttributeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "商品属性分类ID")
    private Long productAttributeCategoryId;

    @Schema(description = "属性名称")
    private String name;

    @Schema(description = "属性选择类型：0->唯一；1->单选；2->多选")
    private Integer selectType;

    @Schema(description = "属性录入方式：0->手工录入；1->从列表中选取")
    private Integer inputType;

    @Schema(description = "可选值列表，以逗号隔开")
    private String inputList;

    @Schema(description = "排序字段：最高的可以单独上传图片")
    private Integer sort;

    @Schema(description = "分类筛选样式：0->普通；1->颜色")
    private Integer filterType;

    @Schema(description = "检索类型：0->不需要进行检索；1->关键字检索；2->范围检索")
    private Integer searchType;

    @Schema(description = "相同属性产品是否关联：0->不关联；1->关联")
    private Integer relatedStatus;

    @Schema(description = "是否支持手动新增：0->不支持；1->支持")
    private Integer handAddStatus;

    @Schema(description = "属性的类型：0->规格；1->参数")
    private Integer type;
}
