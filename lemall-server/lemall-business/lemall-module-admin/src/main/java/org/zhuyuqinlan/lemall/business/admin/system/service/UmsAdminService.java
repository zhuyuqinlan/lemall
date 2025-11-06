package org.zhuyuqinlan.lemall.business.admin.system.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsAdminCreateRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsAdminInfoDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsAdminDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsRoleDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dao.UmsAdminDao;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsAdminUpdateRequestDTO;
import org.zhuyuqinlan.lemall.common.constant.AuthConstant;
import org.zhuyuqinlan.lemall.common.dto.AdminUserDto;
import org.zhuyuqinlan.lemall.common.dto.UmsMenuDTO;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileCacheService;
import org.zhuyuqinlan.lemall.common.mapper.UmsAdminLoginLogMapper;
import org.zhuyuqinlan.lemall.common.mapper.UmsAdminMapper;
import org.zhuyuqinlan.lemall.common.response.BizException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UmsAdminService extends ServiceImpl<UmsAdminMapper, UmsAdmin> {

    private final UmsAdminLoginLogMapper loginLogMapper;
    private final UmsAdminRoleRelationService userRoleRelationService;
    private final UmsRoleService roleService;
    private final UmsAdminDao umsAdminDao;
    private final FileCacheService fileCacheService;

    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public SaTokenInfo login(String username, String password) {
        UmsAdmin umsAdmin = baseMapper.selectOne(Wrappers.<UmsAdmin>lambdaQuery()
                .eq(UmsAdmin::getUsername, username)
        );
        if (umsAdmin == null) throw new BizException("该用户不存在");
        if (!BCrypt.checkpw(password, umsAdmin.getPassword())) throw new BizException("密码不正确！");
        if (umsAdmin.getStatus() != 1) throw new BizException("该账号已被禁用！");

        StpUtil.login(umsAdmin.getId());
        insertAdminUser(umsAdmin.getId());
        umsAdmin.setLoginTime(new Date());
        baseMapper.updateById(umsAdmin);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        insertLoginLog(umsAdmin);
        return saTokenInfo;
    }

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    public UmsAdminInfoDTO getCurrentAdmin() {
        Long id = Long.valueOf(StpUtil.getLoginId().toString());
        AdminUserDto admin = (AdminUserDto) StpUtil.getSession().get(AuthConstant.STP_ADMIN_INFO);
        if (admin == null) admin = insertAdminUser(id);

        UmsAdminInfoDTO res = new UmsAdminInfoDTO();
        BeanUtils.copyProperties(admin, res);
        return res;
    }

    /**
     * 获取用户角色列表
     */
    public List<UmsRoleDTO> getRoleList(Long id) {
        List<UmsRole> roleList = umsAdminDao.getRoleList(id);
        return roleList.stream().map(role -> {
            UmsRoleDTO dto = new UmsRoleDTO();
            BeanUtils.copyProperties(role, dto);
            return dto;
        }).toList();
    }

    /**
     * 分页查询所有用户
     */
    public IPage<UmsAdminDTO> listPage(String keyword, Integer pageSize, Integer pageNum) {
        Page<UmsAdmin> adminPage = super.page(new Page<>(pageNum, pageSize), Wrappers.<UmsAdmin>lambdaQuery()
                .and(StringUtils.hasText(keyword), q -> q
                        .like(UmsAdmin::getUsername, keyword)
                        .or()
                        .like(UmsAdmin::getNickName, keyword))
                .orderByDesc(UmsAdmin::getUpdateTime)
        );
        return adminPage.convert(admin -> {
            UmsAdminDTO dto = new UmsAdminDTO();
            BeanUtils.copyProperties(admin, dto);
            dto.setUrl(fileCacheService.getFileUrlByFileId(admin.getAvatarFileId()));
            return dto;
        });
    }

    /**
     * 用户注册
     */
    public UmsAdminDTO register(UmsAdminCreateRequestDTO umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(umsAdminParam.getStatus());

        List<UmsAdmin> existing = baseMapper.selectList(
                Wrappers.<UmsAdmin>lambdaQuery().eq(UmsAdmin::getUsername, umsAdmin.getUsername())
        );
        if (!existing.isEmpty()) throw new BizException("用户名已存在");

        umsAdmin.setPassword(BCrypt.hashpw(umsAdmin.getPassword()));
        baseMapper.insert(umsAdmin);

        UmsAdminDTO dto = new UmsAdminDTO();
        BeanUtils.copyProperties(umsAdmin, dto);
        return dto;
    }

    /**
     * 更新用户信息
     */
    public boolean updateAdmin(Long id, UmsAdminUpdateRequestDTO umsAdminCreateRequestDTO) {
        if (umsAdminCreateRequestDTO.getUsername() != null) {
            List<UmsAdmin> existing = baseMapper.selectList(
                    Wrappers.<UmsAdmin>lambdaQuery().eq(UmsAdmin::getUsername, umsAdminCreateRequestDTO.getUsername())
            );
            if (!existing.isEmpty() && !existing.get(0).getId().equals(id)) {
                throw new BizException("用户名已存在");
            }
        }

        if (umsAdminCreateRequestDTO.getStatus() == 0) StpUtil.logout(id);

        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setId(id);
        BeanUtils.copyProperties(umsAdminCreateRequestDTO, umsAdmin);
        return super.updateById(umsAdmin);
    }

    /**
     * 删除用户信息
     */
    public boolean deleteAdmin(Long id) {
        List<UmsRole> roleList = umsAdminDao.getRoleList(id);
        roleList.forEach(role -> role.setAdminCount(role.getAdminCount() - 1));
        roleService.updateBatchById(roleList);

        userRoleRelationService.remove(Wrappers.<UmsAdminRoleRelation>lambdaQuery()
                .eq(UmsAdminRoleRelation::getAdminId, id));
        StpUtil.logout(id);

        return super.removeById(id);
    }

    /**
     * 给用户分配角色
     */
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();

        // 原角色计数器-1
        List<UmsRole> oldRoles = umsAdminDao.getRoleList(adminId);
        oldRoles.forEach(role -> role.setAdminCount(role.getAdminCount() - 1));
        roleService.updateBatchById(oldRoles);

        userRoleRelationService.remove(Wrappers.<UmsAdminRoleRelation>lambdaQuery()
                .eq(UmsAdminRoleRelation::getAdminId, adminId));

        List<UmsAdminRoleRelation> newRelations = new ArrayList<>();
        if (roleIds != null) {
            roleIds.forEach(roleId -> {
                UmsAdminRoleRelation relation = new UmsAdminRoleRelation();
                relation.setAdminId(adminId);
                relation.setRoleId(roleId);
                newRelations.add(relation);
            });
        }
        userRoleRelationService.saveBatch(newRelations);

        List<UmsRole> newRoles = roleService.listByIds(roleIds);
        newRoles.forEach(role -> role.setAdminCount(role.getAdminCount() + 1));
        roleService.updateBatchById(newRoles);

        return count;
    }

    /**
     * 添加登录记录
     */
    private void insertLoginLog(UmsAdmin admin) {
        if (admin == null) return;
        UmsAdminLoginLog log = new UmsAdminLoginLog();
        log.setAdminId(admin.getId());
        log.setCreateTime(new Date());

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        log.setIp(request.getRemoteAddr());

        loginLogMapper.insert(log);
    }

    /**
     * 添加用户信息到会话
     */
    private AdminUserDto insertAdminUser(Long id) {
        AdminUserDto dto = new AdminUserDto();
        UmsAdmin admin = super.getById(id);
        BeanUtils.copyProperties(admin, dto);
        String fileUrl = fileCacheService.getFileUrlByFileId(admin.getAvatarFileId());
        dto.setUrl(fileUrl);

        List<UmsResource> resources = umsAdminDao.getResourceList(admin.getId());
        dto.setResourceList(resources.stream().map(UmsResource::getName).toList());

        List<UmsRole> roles = umsAdminDao.getRoleList(admin.getId());
        dto.setRoleList(roles.stream().map(UmsRole::getValue).toList());

        List<UmsPermission> perms = umsAdminDao.getPermissionList(admin.getId());
        dto.setPermissionList(perms.stream().map(UmsPermission::getValue).toList());

        List<UmsMenu> umsMenuList = roleService.getMenuList(admin.getId());
        dto.setMenuList(umsMenuList.stream().map(e -> {
            UmsMenuDTO umsMenuDTO = new UmsMenuDTO();
            BeanUtils.copyProperties(e, umsMenuDTO);
            return umsMenuDTO;
        }).toList());

        StpUtil.getSession().set(AuthConstant.STP_ADMIN_INFO, dto);
        return dto;
    }
}
