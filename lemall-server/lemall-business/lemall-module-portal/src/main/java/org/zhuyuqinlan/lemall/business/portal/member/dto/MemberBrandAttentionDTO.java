package org.zhuyuqinlan.lemall.business.portal.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Schema(description = "会员关注品牌信息")
public class MemberBrandAttentionDTO {

    @Schema(description = "唯一标识")
    private String id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员昵称")
    private String memberNickname;

    @Schema(description = "会员头像URL")
    private String memberIcon;

    @Schema(description = "品牌ID")
    private Long brandId;

    @Schema(description = "品牌名称")
    private String brandName;

    @Schema(description = "品牌Logo URL")
    private String brandLogo;

    @Schema(description = "品牌所在城市")
    private String brandCity;

    @Schema(description = "创建时间")
    private Date createTime;
}
