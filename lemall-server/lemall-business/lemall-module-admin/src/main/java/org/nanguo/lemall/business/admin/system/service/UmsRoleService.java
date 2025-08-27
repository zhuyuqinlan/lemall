package org.nanguo.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.system.dto.request.UmsRoleRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsMenuResponseDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsResourceResponseDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsRoleResponseDTO;
import org.nanguo.lemall.common.entity.UmsMenu;
import org.nanguo.lemall.common.entity.UmsRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UmsRoleService extends IService<UmsRole>{


    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long id);

    /**
     * 分页查询角色列表
     * @param keyword 关键字
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 查询结果
     */
    IPage<UmsRoleResponseDTO> pageRole(String keyword, Integer pageSize, Integer pageNum);


    /**
     * 修改角色信息
     * @param id 角色id
     * @param umsRole 内容
     * @return 受影响的行数
     */
    int updateRole(Long id, UmsRoleRequestDTO umsRole);

    /**
     * 返回所有角色
     * @return 角色
     */
    List<UmsRoleResponseDTO> listAll();

    /**
     * 删除角色
     * @param ids 角色id
     * @return 删除标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean deleteRoles(List<Long> ids);


    /**
     * 添加角色
     * @param role 角色dto
     * @return 是否成功
     */
    boolean create(UmsRoleRequestDTO role);

    /**
     * 根据角色id查菜单
     * @param roleId 角色id
     * @return 菜单
     */
    List<UmsMenuResponseDTO> listMenu(Long roleId);

    /**
     * 跟角色分配菜单
     * @param roleId 角色id
     * @param menuIds 菜单列表
     * @return 分配的条数
     */
    @Transactional(rollbackFor = Exception.class)
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 根据角色id查资源
     * @param roleId 角色id
     * @return 资源列表
     */
    List<UmsResourceResponseDTO> listResource(Long roleId);

    /**
     * 给用户分配资源
     * @param roleId 用户id
     * @param resourceIds 资源id列表
     * @return 受影响的行数
     */
    int allocResource(Long roleId, List<Long> resourceIds);
}
