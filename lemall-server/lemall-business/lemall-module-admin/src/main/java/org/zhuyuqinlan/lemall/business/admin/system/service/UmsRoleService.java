package org.zhuyuqinlan.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.system.dao.UmsRoleDao;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsRoleRequestDTO;
import org.zhuyuqinlan.lemall.common.dto.UmsMenuDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsResourceDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsRoleDTO;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.mapper.UmsRoleMapper;
import org.zhuyuqinlan.lemall.common.response.BizException;

import java.util.List;
import java.util.Objects;

/**
 * 后台角色管理Service
 */
@Service
@RequiredArgsConstructor
public class UmsRoleService extends ServiceImpl<UmsRoleMapper, UmsRole> {

    private final UmsRoleMenuRelationService umsRoleMenuRelationService;
    private final UmsAdminRoleRelationService umsAdminRoleRelationService;
    private final UmsRoleResourceRelationService umsRoleResourceRelationService;
    private final UmsResourceService umsResourceService;
    private final UmsRoleDao umsRoleDao;

    public List<UmsMenu> getMenuList(Long id) {
        return umsRoleDao.getMenuList(id);
    }

    public IPage<UmsRoleDTO> pageRole(String keyword, Integer pageSize, Integer pageNum) {
        return baseMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize),
                com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsRole>lambdaQuery()
                        .like(StringUtils.hasText(keyword), UmsRole::getName, keyword)
                        .orderByDesc(UmsRole::getSort)
                        .orderByDesc(UmsRole::getCreateTime)
        ).convert(umsRole -> {
            UmsRoleDTO dto = new UmsRoleDTO();
            org.springframework.beans.BeanUtils.copyProperties(umsRole, dto);
            return dto;
        });
    }

    public int updateRole(Long id, UmsRoleRequestDTO umsRole) {
        if (umsRole.getName() != null) {
            List<UmsRole> umsRoles = baseMapper.selectList(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsRole>lambdaQuery().eq(UmsRole::getName, umsRole.getName()));
            if (!umsRoles.isEmpty()) {
                Long tempId = umsRoles.get(0).getId();
                if (!Objects.equals(tempId, id)) {
                    throw new BizException("该角色名称已存在");
                }
            }
        }
        UmsRole role = new UmsRole();
        role.setId(id);
        org.springframework.beans.BeanUtils.copyProperties(umsRole, role);
        return baseMapper.updateById(role);
    }

    public List<UmsRoleDTO> listAll() {
        return super.list().stream().map(umsRole -> {
            UmsRoleDTO dto = new UmsRoleDTO();
            org.springframework.beans.BeanUtils.copyProperties(umsRole, dto);
            return dto;
        }).toList();
    }

    public boolean deleteRoles(List<Long> ids) {
        umsRoleMenuRelationService.remove(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsRoleMenuRelation>lambdaQuery().in(UmsRoleMenuRelation::getRoleId, ids));
        umsAdminRoleRelationService.remove(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsAdminRoleRelation>lambdaQuery().in(UmsAdminRoleRelation::getRoleId, ids));
        return super.removeByIds(ids);
    }

    public boolean create(UmsRoleRequestDTO role) {
        UmsRole umsRole = new UmsRole();
        org.springframework.beans.BeanUtils.copyProperties(role, umsRole);
        umsRole.setAdminCount(0);
        return super.save(umsRole);
    }

    public List<UmsMenuDTO> listMenu(Long roleId) {
        return umsRoleDao.getMenuListByRoleId(roleId);
    }

    public int allocMenu(Long roleId, List<Long> menuIds) {
        umsRoleMenuRelationService.remove(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsRoleMenuRelation>lambdaQuery().eq(UmsRoleMenuRelation::getRoleId, roleId));
        List<UmsRoleMenuRelation> relationList = menuIds.stream().map(id -> {
            UmsRoleMenuRelation rel = new UmsRoleMenuRelation();
            rel.setRoleId(roleId);
            rel.setMenuId(id);
            return rel;
        }).toList();
        umsRoleMenuRelationService.saveBatch(relationList);
        return menuIds.size();
    }

    public List<UmsResourceDTO> listResource(Long roleId) {
        return umsRoleDao.getResourceListByRoleId(roleId);
    }

    public int allocResource(Long roleId, List<Long> resourceIds) {
        umsRoleResourceRelationService.remove(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UmsRoleResourceRelation>lambdaQuery().eq(UmsRoleResourceRelation::getRoleId, roleId));
        List<UmsRoleResourceRelation> relationList = resourceIds.stream().map(id -> {
            UmsRoleResourceRelation rel = new UmsRoleResourceRelation();
            rel.setRoleId(roleId);
            rel.setResourceId(id);
            return rel;
        }).toList();
        umsRoleResourceRelationService.saveBatch(relationList);
        umsResourceService.initPathResourceMap();
        return resourceIds.size();
    }
}
