package org.zhuyuqinlan.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.admin.product.dto.validator.FlagValidator;

import java.util.List;

@Getter
@Setter
@Schema(description = "产品分类请求DTO")
public class PmsProductCategoryRequestDTO {
    private Long id;

    @Schema(description = "上级分类的编号：0表示一级分类")
    private Long parentId;

    @NotEmpty
    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "商品单位")
    private String productUnit;

    @FlagValidator(value = {"0","1"},message = "状态只能为0或1")
    @Schema(description = "是否显示在导航栏：0->不显示；1->显示")
    private Integer navStatus;

    @FlagValidator(value = {"0","1"},message = "状态只能为0或1")
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

    @Schema(description = "产品相关筛选属性集合")
    private List<Long> productAttributeIdList;
}
