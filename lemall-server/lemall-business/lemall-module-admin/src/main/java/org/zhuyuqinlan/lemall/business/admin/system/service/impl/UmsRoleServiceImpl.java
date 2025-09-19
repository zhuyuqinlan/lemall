package org.zhuyuqinlan.lemall.business.admin.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsRoleRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsMenuResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsResourceResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsRoleResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.business.admin.system.service.*;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.response.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.system.mapper.UmsRoleMapper;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.system.service.*;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper, UmsRole> implements UmsRoleService {

    private final UmsRoleMenuRelationService umsRoleMenuRelationService;
    private final UmsAdminRoleRelationService umsAdminRoleRelationService;
    private final UmsRoleResourceRelationService umsRoleResourceRelationService;
    private final UmsResourceService umsResourceService;

    @Override
    public List<UmsMenu> getMenuList(Long id) {
        return baseMapper.getMenuList(id);
    }

    @Override
    public IPage<UmsRoleResponseDTO> pageRole(String keyword, Integer pageSize, Integer pageNum) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<UmsRole>lambdaQuery()
                .like(StringUtils.hasText(keyword), UmsRole::getName, keyword)
                .orderByDesc(UmsRole::getSort)
                .orderByDesc(UmsRole::getCreateTime)
        ).convert(umsRole -> {
            UmsRoleResponseDTO umsRoleResponseDTO = new UmsRoleResponseDTO();
            BeanUtils.copyProperties(umsRole, umsRoleResponseDTO);
            return umsRoleResponseDTO;
        });
    }

    @Override
    public int updateRole(Long id, UmsRoleRequestDTO umsRole) {
        // 如果name不为null，说明是修改信息
        if (umsRole.getName() != null) {
            List<UmsRole> umsRoles = baseMapper.selectList(Wrappers.<UmsRole>lambdaQuery().eq(UmsRole::getName, umsRole.getName()));
            if (!umsRoles.isEmpty()) {
                Long tempId = umsRoles.get(0).getId();
                if (!Objects.equals(tempId, id)) {
                    throw new BizException("该角色名称已存在");
                }
            }
        }

        UmsRole role = new UmsRole();
        role.setId(id);
        BeanUtils.copyProperties(umsRole, role);
        return baseMapper.updateById(role);
    }

    @Override
    public List<UmsRoleResponseDTO> listAll() {
        return super.list().stream().map(umsRole -> {
            UmsRoleResponseDTO umsRoleResponseDTO = new UmsRoleResponseDTO();
            BeanUtils.copyProperties(umsRole, umsRoleResponseDTO);
            return umsRoleResponseDTO;
        }).toList();
    }

    @Override
    public boolean deleteRoles(List<Long> ids) {
        umsRoleMenuRelationService.remove(Wrappers.<UmsRoleMenuRelation>lambdaQuery().in(UmsRoleMenuRelation::getRoleId, ids));
        umsAdminRoleRelationService.remove(Wrappers.<UmsAdminRoleRelation>lambdaQuery().in(UmsAdminRoleRelation::getRoleId, ids));
        return super.removeByIds(ids);
    }

    @Override
    public boolean create(UmsRoleRequestDTO role) {
        UmsRole umsRole = new UmsRole();
        BeanUtils.copyProperties(role, umsRole);
        umsRole.setAdminCount(0);
        return super.save(umsRole);
    }

    @Override
    public List<UmsMenuResponseDTO> listMenu(Long roleId) {
        return baseMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        umsRoleMenuRelationService.remove(Wrappers.<UmsRoleMenuRelation>lambdaQuery().eq(UmsRoleMenuRelation::getRoleId, roleId));
        //批量插入新关系
        List<UmsRoleMenuRelation> relationList = menuIds.stream().map(id -> {
            UmsRoleMenuRelation umsRoleMenuRelation = new UmsRoleMenuRelation();
            umsRoleMenuRelation.setRoleId(roleId);
            umsRoleMenuRelation.setMenuId(id);
            return umsRoleMenuRelation;
        }).toList();
        umsRoleMenuRelationService.saveBatch(relationList);
        return menuIds.size();
    }

    @Override
    public List<UmsResourceResponseDTO> listResource(Long roleId) {
        return baseMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        umsRoleResourceRelationService.remove(Wrappers.<UmsRoleResourceRelation>lambdaQuery().eq(UmsRoleResourceRelation::getRoleId,roleId));
        //批量插入新关系
        List<UmsRoleResourceRelation> relationList = resourceIds.stream().map(id -> {
            UmsRoleResourceRelation umsRoleResourceRelation = new UmsRoleResourceRelation();
            umsRoleResourceRelation.setRoleId(roleId);
            umsRoleResourceRelation.setResourceId(id);
            return umsRoleResourceRelation;
        }).toList();
        umsRoleResourceRelationService.saveBatch(relationList);

        umsResourceService.initPathResourceMap();
        return resourceIds.size();
    }
}
