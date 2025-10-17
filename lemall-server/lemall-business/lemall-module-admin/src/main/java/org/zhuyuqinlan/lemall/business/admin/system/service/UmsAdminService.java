package org.zhuyuqinlan.lemall.business.admin.system.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsAdminRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsAdminInfoResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsAdminResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsRoleResponseDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zhuyuqinlan.lemall.common.entity.UmsAdmin;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UmsAdminService extends IService<UmsAdmin>{

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    SaTokenInfo login(String username, String password);

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    UmsAdminInfoResponseDTO getCurrentAdmin();

    /**
     * 获取用户对于角色
     */
    List<UmsRoleResponseDTO> getRoleList(Long id);

    /**
     * 分页查询所有用户
     * @param keyword 关键词
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 查询结果
     */
    IPage<UmsAdminResponseDTO> listPage(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 用户注册
     * @param umsAdminParam 注册参数
     * @return 注册后用户信息
     */
    UmsAdminResponseDTO register(UmsAdminRequestDTO umsAdminParam);

    /**
     * 更改用户信息
     * @param id 用户id
     * @param umsAdminRequestDTO 用户信息
     * @return 更新结果
     */
    boolean updateAdmin(Long id, UmsAdminRequestDTO umsAdminRequestDTO);


    /**
     * 删除用户信息
     * @param id 用户id
     * @return 删除标志
     */
    @Transactional
    boolean deleteAdmin(Long id);

    /**
     * 给用户分配角色
     * @param adminId 用户id
     * @param roleIds 角色id
     * @return 受影响行数
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);
}
