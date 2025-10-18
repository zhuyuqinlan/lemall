package org.zhuyuqinlan.lemall.business.admin.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsResourceCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsResourceCategoryResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsResource;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsResourceService;
import org.zhuyuqinlan.lemall.common.response.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.UmsResourceCategory;
import org.zhuyuqinlan.lemall.common.mapper.UmsResourceCategoryMapper;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsResourceCategoryService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UmsResourceCategoryServiceImpl extends ServiceImpl<UmsResourceCategoryMapper, UmsResourceCategory> implements UmsResourceCategoryService {

    private final UmsResourceService umsResourceService;

    @Override
    public List<UmsResourceCategoryResponseDTO> listAllResourceCategory() {
        return super.list(Wrappers.<UmsResourceCategory>lambdaQuery().orderByDesc(UmsResourceCategory::getSort).orderByDesc(UmsResourceCategory::getCreateTime)).stream().map(e -> {
            UmsResourceCategoryResponseDTO dto = new UmsResourceCategoryResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }

    @Override
    public boolean create(UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        UmsResourceCategory umsResourceCategory = new UmsResourceCategory();
        BeanUtils.copyProperties(umsResourceCategoryRequestDTO, umsResourceCategory);
        return super.save(umsResourceCategory);
    }

    @Override
    public boolean updateRes(Long id, UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO) {
        UmsResourceCategory umsResourceCategory = new UmsResourceCategory();
        BeanUtils.copyProperties(umsResourceCategoryRequestDTO, umsResourceCategory);
        umsResourceCategory.setId(id);
        return super.updateById(umsResourceCategory);
    }

    @Override
    public boolean delete(Long id) {
        // 检查该分类下有没有菜单项
        List<UmsResource> list = umsResourceService.list(Wrappers.<UmsResource>lambdaQuery().eq(UmsResource::getCategoryId, id));
        if (list != null && !list.isEmpty()) {
            throw new BizException("该菜单分类下还有菜单项");
        }
        return super.removeById(id);
    }
}
