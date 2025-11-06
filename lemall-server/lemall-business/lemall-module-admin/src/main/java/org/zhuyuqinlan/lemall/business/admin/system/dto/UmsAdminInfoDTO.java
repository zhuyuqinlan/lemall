package org.zhuyuqinlan.lemall.business.admin.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.common.dto.UmsMenuDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsMenu;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Schema(description = "当前用户信息响应dto")
public class UmsAdminInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "网名")
    private String nickName;

    @Schema(description = "头像")
    private String url;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建时间")
    private Date updateTime;

    @Schema(description = "登录时间")
    private Date loginTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "菜单列表")
    private List<UmsMenuDTO> menuList;
}
