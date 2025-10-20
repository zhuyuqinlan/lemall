package org.zhuyuqinlan.lemall.business.portal.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(description = "会员浏览记录 DTO")
public class MemberReadHistoryResponseDTO {

    @Schema(description = "记录ID", example = "64f7c2b1e3a7f12d34567890")
    private String id;

    @Schema(description = "会员ID", example = "123456")
    private Long memberId;

    @Schema(description = "会员昵称", example = "张三")
    private String memberNickname;

    @Schema(description = "会员头像URL", example = "https://example.com/avatar.png")
    private String memberIcon;

    @Schema(description = "商品ID", example = "987654")
    private Long productId;

    @Schema(description = "商品名称", example = "苹果 iPhone 15")
    private String productName;

    @Schema(description = "商品图片URL", example = "https://example.com/product.png")
    private String productPic;

    @Schema(description = "商品副标题", example = "官方旗舰店")
    private String productSubTitle;

    @Schema(description = "商品价格", example = "6999.00")
    private String productPrice;

    @Schema(description = "创建时间", example = "2025-10-20T17:00:00Z")
    private Date createTime;
}
