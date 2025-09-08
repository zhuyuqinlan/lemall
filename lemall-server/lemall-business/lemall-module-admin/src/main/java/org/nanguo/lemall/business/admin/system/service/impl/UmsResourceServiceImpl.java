package org.nanguo.lemall.business.admin.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.common.constant.AuthConstant;
import org.nanguo.lemall.business.admin.system.dto.request.UmsResourceRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsResourceResponseDTO;
import org.nanguo.lemall.common.entity.UmsResource;
import org.nanguo.lemall.common.entity.UmsRoleResourceRelation;
import org.nanguo.lemall.business.admin.system.mapper.UmsResourceMapper;
import org.nanguo.lemall.business.admin.system.service.UmsResourceService;
import org.nanguo.lemall.business.admin.system.service.UmsRoleResourceRelationService;
import org.nanguo.lemall.common.service.RedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 后台资源管理Service实现类
 */
@Service
@RequiredArgsConstructor
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceMapper, UmsResource> implements UmsResourceService {

    private final RedisService redisService;
    private final UmsResourceMapper umsResourceMapper;
    private final UmsRoleResourceRelationService umsRoleResourceRelationService;
    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;

    @Override
    public void initPathResourceMap() {
        Map<String, String> pathResourceMap = new TreeMap<>();
        List<UmsResource> resourceList = umsResourceMapper.selectList(null);
        for (UmsResource resource : resourceList) {
            pathResourceMap.put(adminPrefix + resource.getUrl(), resource.getId().toString());
        }
        redisService.del(AuthConstant.PATH_RESOURCE_MAP);
        redisService.hSetAll(AuthConstant.PATH_RESOURCE_MAP, pathResourceMap);
    }

    @Override
    public List<UmsResourceResponseDTO> listAll() {
        return super.list().stream().map(e -> {
            UmsResourceResponseDTO umsResourceResponseDTO = new UmsResourceResponseDTO();
            BeanUtils.copyProperties(e, umsResourceResponseDTO);
            return umsResourceResponseDTO;
        }).toList();
    }

    @Override
    public IPage<UmsResourceResponseDTO> pageRes(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize),
                        Wrappers.<UmsResource>lambdaQuery()
                                .eq(categoryId != null, UmsResource::getCategoryId, categoryId)
                                .like(StringUtils.hasText(nameKeyword), UmsResource::getName, nameKeyword)
                                .like(StringUtils.hasText(urlKeyword), UmsResource::getUrl, urlKeyword)
                                .orderByDesc(UmsResource::getCreateTime)
                )
                .convert(e -> {
                    UmsResourceResponseDTO umsResourceResponseDTO = new UmsResourceResponseDTO();
                    BeanUtils.copyProperties(e, umsResourceResponseDTO);
                    return umsResourceResponseDTO;
                });
    }

    @Override
    public boolean create(UmsResourceRequestDTO umsResource) {
        UmsResource umsResource1 = new UmsResource();
        BeanUtils.copyProperties(umsResource, umsResource1);
        boolean save = super.save(umsResource1);
        initPathResourceMap();
        return save;
    }

    @Override
    public boolean updateRes(Long id, UmsResourceRequestDTO umsResourceRequestDTO) {
        UmsResource umsResource = new UmsResource();
        BeanUtils.copyProperties(umsResourceRequestDTO, umsResource);
        umsResource.setId(id);
        boolean b = super.updateById(umsResource);
        initPathResourceMap();
        return b;
    }

    @Override
    public boolean deleteRes(Long id) {
        umsRoleResourceRelationService.remove(Wrappers.<UmsRoleResourceRelation>lambdaQuery().eq(UmsRoleResourceRelation::getResourceId, id));
        boolean b = super.removeById(id);
        initPathResourceMap();
        return b;
    }
}
