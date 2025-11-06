package org.zhuyuqinlan.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsResourceRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.UmsResourceDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsResource;
import org.zhuyuqinlan.lemall.common.entity.UmsRoleResourceRelation;
import org.zhuyuqinlan.lemall.common.mapper.UmsResourceMapper;
import org.zhuyuqinlan.lemall.common.service.RedisService;


@Service
@RequiredArgsConstructor
public class UmsResourceService extends ServiceImpl<UmsResourceMapper, UmsResource> {

    private final RedisService redisService;
    private final UmsResourceMapper umsResourceMapper;
    private final UmsRoleResourceRelationService umsRoleResourceRelationService;

    @Value("${lemall.server.prefix.admin}")
    private String adminPrefix;
    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;
    @Value("${redis.key.auth.pathResourceMap}")
    private String AUTH_PATH_RESOURCE_MAP;

    /**
     * 初始化路径与资源访问规则
     */
    public void initPathResourceMap() {
        Map<String, String> pathResourceMap = new TreeMap<>();
        List<UmsResource> resourceList = umsResourceMapper.selectList(null);
        for (UmsResource resource : resourceList) {
            pathResourceMap.put(adminPrefix + resource.getUrl(), resource.getName());
        }
        String key = REDIS_PREFIX + ":" + AUTH_PATH_RESOURCE_MAP;
        redisService.del(key);
        redisService.hSetAll(key, pathResourceMap);
    }

    /**
     * 列出所有资源
     */
    public List<UmsResourceDTO> listAll() {
        return super.list().stream().map(e -> {
            UmsResourceDTO umsResourceDTO = new UmsResourceDTO();
            BeanUtils.copyProperties(e, umsResourceDTO);
            return umsResourceDTO;
        }).toList();
    }

    /**
     * 分页查询资源列表
     */
    public IPage<UmsResourceDTO> pageRes(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize),
                        Wrappers.<UmsResource>lambdaQuery()
                                .eq(categoryId != null, UmsResource::getCategoryId, categoryId)
                                .like(StringUtils.hasText(nameKeyword), UmsResource::getName, nameKeyword)
                                .like(StringUtils.hasText(urlKeyword), UmsResource::getUrl, urlKeyword)
                                .orderByDesc(UmsResource::getUpdateTime)
                )
                .convert(e -> {
                    UmsResourceDTO umsResourceDTO = new UmsResourceDTO();
                    BeanUtils.copyProperties(e, umsResourceDTO);
                    return umsResourceDTO;
                });
    }

    /**
     * 添加资源
     */
    public boolean create(UmsResourceRequestDTO umsResource) {
        UmsResource umsResource1 = new UmsResource();
        BeanUtils.copyProperties(umsResource, umsResource1);
        boolean save = super.save(umsResource1);
        initPathResourceMap();
        return save;
    }

    /**
     * 修改资源
     */
    public boolean updateRes(Long id, UmsResourceRequestDTO umsResourceRequestDTO) {
        UmsResource umsResource = new UmsResource();
        BeanUtils.copyProperties(umsResourceRequestDTO, umsResource);
        umsResource.setId(id);
        boolean b = super.updateById(umsResource);
        initPathResourceMap();
        return b;
    }

    /**
     * 根据id删除资源
     */
    public boolean deleteRes(Long id) {
        umsRoleResourceRelationService.remove(
                Wrappers.<UmsRoleResourceRelation>lambdaQuery().eq(UmsRoleResourceRelation::getResourceId, id)
        );
        boolean b = super.removeById(id);
        initPathResourceMap();
        return b;
    }
}
