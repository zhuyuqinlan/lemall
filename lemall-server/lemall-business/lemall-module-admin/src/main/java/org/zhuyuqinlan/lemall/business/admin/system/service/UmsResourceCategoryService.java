package org.zhuyuqinlan.lemall.business.admin.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.List;

import org.zhuyuqinlan.lemall.common.entity.UmsResourceCategory;
import org.zhuyuqinlan.lemall.common.mapper.UmsResourceCategoryMapper;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsResourceCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsResourceCategoryDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsResource;
import org.zhuyuqinlan.lemall.common.response.BizException;

/**
 * 资源分类服务类
 */
@Service
@RequiredArgsConstructor
public class UmsResourceCategoryService extends ServiceImpl<UmsResourceCategoryMapper, UmsResourceCategory> {

    private final UmsResourceService umsResourceService;

    /**
     * 列出所有资源分类
     */
    public List<UmsResourceCategoryDTO> listAllResourceCategory() {
        return super.list(Wrappers.<UmsResourceCategory>lambdaQuery()
                        .orderByDesc(UmsResourceCategory::getSort)
                        .orderByDesc(UmsResourceCategory::getUpdateTime))
                .stream().map(e -> {
                    UmsResourceCategoryDTO dto = new UmsResourceCategoryDTO();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                }).toList();
    }

    /**
     * 添加资源分类
     */
    public boolean create(UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        UmsResourceCategory umsResourceCategory = new UmsResourceCategory();
        BeanUtils.copyProperties(umsResourceCategoryRequestDTO, umsResourceCategory);
        return super.save(umsResourceCategory);
    }

    /**
     * 修改资源分类
     */
    public boolean updateRes(Long id, UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        UmsResourceCategory umsResourceCategory = new UmsResourceCategory();
        BeanUtils.copyProperties(umsResourceCategoryRequestDTO, umsResourceCategory);
        umsResourceCategory.setId(id);
        return super.updateById(umsResourceCategory);
    }

    /**
     * 删除资源分类
     */
    public boolean delete(Long id) {
        List<UmsResource> list = umsResourceService.list(
                Wrappers.<UmsResource>lambdaQuery().eq(UmsResource::getCategoryId, id)
        );
        if (list != null && !list.isEmpty()) {
            throw new BizException("该菜单分类下还有菜单项");
        }
        return super.removeById(id);
    }
}
