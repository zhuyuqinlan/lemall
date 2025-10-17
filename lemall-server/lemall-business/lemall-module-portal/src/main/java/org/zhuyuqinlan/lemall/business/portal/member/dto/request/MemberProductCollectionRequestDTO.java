package org.zhuyuqinlan.lemall.business.portal.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Getter
@Setter
@Schema(description = "会员收藏的商品信息")
public class MemberProductCollectionRequestDTO {

    @Schema(description = "唯一标识")
    private String id;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员昵称")
    private String memberNickname;

    @Schema(description = "会员头像URL")
    private String memberIcon;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品主图URL")
    private String productPic;

    @Schema(description = "商品副标题")
    private String productSubTitle;

    @Schema(description = "商品价格")
    private String productPrice;

    @Schema(description = "创建时间")
    private Date createTime;
}

