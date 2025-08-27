package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限)
 */
@TableName(value = "ums_admin_permission_relation")
public class UmsAdminPermissionRelation {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "admin_id")
    private Long adminId;

    @TableField(value = "permission_id")
    private Long permissionId;

    @TableField(value = "`type`")
    private Integer type;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return admin_id
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * @return permission_id
     */
    public Long getPermissionId() {
        return permissionId;
    }

    /**
     * @param permissionId
     */
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }
}