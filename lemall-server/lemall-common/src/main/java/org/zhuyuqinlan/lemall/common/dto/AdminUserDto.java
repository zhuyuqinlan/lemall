package org.zhuyuqinlan.lemall.common.dto;



import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@Schema(name = "AdminUserDto", description = "后台管理用户DTO")
public class AdminUserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "网名")
    private String nickName;

    @Schema(description = "头像")
    private String icon;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "登录时间")
    private Date loginTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "权限列表")
    private List<String> permissionList;

    @Schema(description = "角色列表")
    private List<String> roleList;

    @Schema(description = "资源列表")
    private List<String> resourceList;

    @Schema(description = "菜单列表")
    private List<UmsMenuDTO> menuList;
}

