package org.zhuyuqinlan.lemall.business.admin.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zhuyuqinlan.lemall.business.admin.system.dto.request.UmsResourceRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.system.dto.response.UmsResourceResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台资源管理Service
 */
public interface UmsResourceService extends IService<UmsResource> {

    /**
     * 初始化路径与资源访问规则
     */
    void initPathResourceMap();

    /**
     * 列出所有资源
     * @return 资源列表
     */
    List<UmsResourceResponseDTO> listAll();

    /**
     * 分页查询资源列表
     * @param categoryId 分类id
     * @param nameKeyword 名字关键字
     * @param urlKeyword url关键词
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 查询结果
     */
    IPage<UmsResourceResponseDTO> pageRes(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize);

    /**
     * 添加资源
     * @param umsResource 请求参数
     * @return 成功标志
     */
    boolean create(UmsResourceRequestDTO umsResource);

    /**
     * 修改资源
     * @param id id
     * @param umsResourceRequestDTO 参数
     * @return 成功标志
     */
    boolean updateRes(Long id, UmsResourceRequestDTO umsResourceRequestDTO);

    /**
     * 根据id删除资源
     * @param id id
     * @return 成功标志
     */
    @Transactional
    boolean deleteRes(Long id);
}
