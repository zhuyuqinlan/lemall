package org.zhuyuqinlan.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsMenuRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsMenuNodeResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsMenuResponseDTO;
import org.zhuyuqinlan.lemall.common.mapper.UmsMenuMapper;
import org.zhuyuqinlan.lemall.common.entity.UmsMenu;
import org.zhuyuqinlan.lemall.common.entity.UmsRoleMenuRelation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UmsMenuService extends ServiceImpl<UmsMenuMapper, UmsMenu> {

    private final UmsRoleMenuRelationService roleMenuRelationService;

    /**
     * 树结构返回菜单
     * @return 结果
     */
    public List<UmsMenuNodeResponseDTO> treeList() {
        List<UmsMenuNodeResponseDTO> menuList = super.list(Wrappers.<UmsMenu>lambdaQuery()
                        .orderByDesc(UmsMenu::getSort)
                        .orderByDesc(UmsMenu::getCreateTime))
                .stream()
                .map(umsMenu -> {
                    UmsMenuNodeResponseDTO dto = new UmsMenuNodeResponseDTO();
                    BeanUtils.copyProperties(umsMenu, dto);
                    return dto;
                }).toList();

        return menuList.stream()
                .filter(menu -> menu.getParentId() == 0L)
                .map(menu -> buildMenuTree(menu, menuList))
                .toList();
    }

    /**
     * 分页查询菜单
     */
    public IPage<UmsMenuResponseDTO> pageMenu(Long parentId, Integer pageNum, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize),
                        Wrappers.<UmsMenu>lambdaQuery()
                                .eq(UmsMenu::getParentId, parentId)
                                .orderByDesc(UmsMenu::getSort)
                                .orderByDesc(UmsMenu::getCreateTime))
                .convert(umsMenu -> {
                    UmsMenuResponseDTO dto = new UmsMenuResponseDTO();
                    BeanUtils.copyProperties(umsMenu, dto);
                    return dto;
                });
    }

    /**
     * 创建菜单
     */
    public int create(UmsMenuRequestDTO umsMenuParamRequestDTO) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(umsMenuParamRequestDTO, umsMenu);
        updateLevel(umsMenu);
        return baseMapper.insert(umsMenu);
    }

    /**
     * 根据id获取菜单
     */
    public UmsMenuResponseDTO getItem(Long id) {
        UmsMenu umsMenu = super.getById(id);
        UmsMenuResponseDTO dto = new UmsMenuResponseDTO();
        BeanUtils.copyProperties(umsMenu, dto);
        return dto;
    }

    /**
     * 修改后台菜单
     */
    public boolean updateMenu(UmsMenuRequestDTO umsMenuParamRequestDTO, Long id) {
        UmsMenu umsMenu = new UmsMenu();
        BeanUtils.copyProperties(umsMenuParamRequestDTO, umsMenu);
        umsMenu.setId(id);
        if (umsMenu.getParentId() != null) {
            updateLevel(umsMenu);
        }
        return super.updateById(umsMenu);
    }

    /**
     * 删除后台菜单
     */
    @Transactional
    public boolean deleteMenu(Long id) {
        roleMenuRelationService.remove(Wrappers.<UmsRoleMenuRelation>lambdaQuery()
                .eq(UmsRoleMenuRelation::getMenuId, id));
        return super.removeById(id);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            umsMenu.setLevel(0);
        } else {
            UmsMenu parentMenu = baseMapper.selectList(
                    Wrappers.<UmsMenu>lambdaQuery().eq(UmsMenu::getId, umsMenu.getParentId())
            ).stream().findFirst().orElse(null);
            if (parentMenu != null) umsMenu.setLevel(parentMenu.getLevel() + 1);
            else umsMenu.setLevel(0);
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
