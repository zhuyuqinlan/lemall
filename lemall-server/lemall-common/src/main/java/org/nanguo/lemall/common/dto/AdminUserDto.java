package org.nanguo.lemall.common.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 权限框架用的服装类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 用户id

    private String username; // 用户名

    private String nickName; // 网名

    private String icon; // 头像

    private String email; // 邮箱

    private Date createTime; // 创建时间

    private Date loginTime; // 登录时间

    private Integer status; // 状态

    private List<String> permissionList; // 权限列表

    private List<String> roleList; // 角色列表

    private List<String> resourceList; // 资源列表

    private List<String> menuList; // 菜单列表
}
