package org.nanguo.lemall.business.admin.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.system.dto.request.UmsMenuRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsMenuNodeResponseDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsMenuResponseDTO;
import org.nanguo.lemall.common.entity.*;
import org.nanguo.lemall.business.admin.system.service.UmsRoleMenuRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.system.mapper.UmsMenuMapper;
import org.nanguo.lemall.business.admin.system.service.UmsMenuService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UmsMenuServiceImpl extends ServiceImpl<UmsMenuMapper, UmsMenu> implements UmsMenuService {

    private final UmsRoleMenuRelationService roleMenuRelationService;

    @Override
    public List<UmsMenuNodeResponseDTO> treeList() {
        // 先把 UmsMenu 转成 DTO 列表
        List<UmsMenuNodeResponseDTO> menuList = super.list(Wrappers.<UmsMenu>lambdaQuery().orderByDesc(UmsMenu::getSort).orderByDesc(UmsMenu::getCreateTime)).stream().map(umsMenu -> {
            UmsMenuNodeResponseDTO dto = new UmsMenuNodeResponseDTO();
            BeanUtils.copyProperties(umsMenu, dto);
            return dto;
        }).toList();

        // 构建树形结构（只取 parentId=0 的作为根节点）
        return menuList.stream()
                .filter(menu -> menu.getParentId() == 0L)
                .map(menu -> buildMenuTree(menu, menuList))
                .toList();
    }

    @Override
    public IPage<UmsMenuResponseDTO> pageMenu(Long parentId, Integer pageNum, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<UmsMenu>lambdaQuery()
                .eq(UmsMenu::getParentId, parentId)
                .orderByDesc(UmsMenu::getSort)
                .orderByDesc(UmsMenu::getCreateTime)
        ).convert(umsMenu -> {
            UmsMenuResponseDTO dto = new UmsMenuResponseDTO();
            BeanUtils.copyProperties(umsMenu, dto);
            return dto;
        });
    }

    @Override
    public int create(UmsMenuRequestDTO umsMenuParamRequestDTO) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(umsMenuParamRequestDTO, umsMenu);
        updateLevel(umsMenu);
        return baseMapper.insert(umsMenu);
    }

    @Override
    public UmsMenuResponseDTO getItem(Long id) {
        UmsMenu umsMenu = super.getById(id);
        UmsMenuResponseDTO dto = new UmsMenuResponseDTO();
        BeanUtils.copyProperties(umsMenu, dto);
        return dto;
    }

    @Override
    public boolean updateMenu(UmsMenuRequestDTO umsMenuParamRequestDTO, Long id) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(umsMenuParamRequestDTO, umsMenu);
        umsMenu.setId(id);
        if (umsMenu.getParentId() != null) {
            updateLevel(umsMenu);
        }
        return super.updateById(umsMenu);
    }

    @Override
    public boolean deleteMenu(Long id) {
        roleMenuRelationService.remove(Wrappers.<UmsRoleMenuRelation>lambdaQuery().eq(UmsRoleMenuRelation::getMenuId, id));
        return super.removeById(id);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            UmsMenu parentMenu = baseMapper.selectList(Wrappers.<UmsMenu>lambdaQuery().eq(UmsMenu::getId, umsMenu.getParentId())).get(0);
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    /**
     * 递归构建菜单树
     */
    private UmsMenuNodeResponseDTO buildMenuTree(UmsMenuNodeResponseDTO menu, List<UmsMenuNodeResponseDTO> menuList) {
        List<UmsMenuNodeResponseDTO> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> buildMenuTree(subMenu, menuList))
                .toList();
        menu.setChildren(children);
        return menu;
    }
}
