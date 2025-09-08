package org.nanguo.lemall.business.admin.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.nanguo.lemall.business.admin.product.dto.validator.FlagValidator;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(name = "品牌请求DTO")
public class PmsBrandRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Schema(description = "品牌名称")
    private String name;

    @Schema(description = "首字母")
    private String firstLetter;

    @Min(value = 0)
    @Schema(description = "排序")
    private Integer sort;

    @FlagValidator(value = {"0","1"},message = "厂商状态不正确")
    @Schema(description = "是否为品牌制造商：0->不是；1->是")
    private Integer factoryStatus;

    @FlagValidator(value = {"0","1"},message = "显示状态不正确")
    @Schema(description = "显示状态：0->不显示；1->显示")
    private Integer showStatus;

    @NotBlank
    @Schema(description = "品牌logo")
    private String logo;
    
    @Schema(description = "专区大图")
    private String bigPic;

    @Schema(description = "品牌故事")
    private String brandStory;
}
