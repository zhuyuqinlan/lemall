package org.zhuyuqinlan.lemall.business.admin.system.component;

import jakarta.annotation.PostConstruct;
import org.zhuyuqinlan.lemall.business.admin.system.service.UmsResourceService;
import org.springframework.stereotype.Component;

/**
 * 启动时将路径和权限关系添加到redis
 */
@Component
public class PathResourceRulesHolder {

    private final UmsResourceService resourceService;

    public PathResourceRulesHolder(UmsResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostConstruct
    public void initPathResourceMap(){
        resourceService.initPathResourceMap();
    }
}
