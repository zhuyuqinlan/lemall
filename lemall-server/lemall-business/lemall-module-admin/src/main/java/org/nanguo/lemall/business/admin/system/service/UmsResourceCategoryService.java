package org.nanguo.lemall.business.admin.system.service;

import org.nanguo.lemall.business.admin.system.dto.request.UmsResourceCategoryRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsResourceCategoryResponseDTO;
import org.nanguo.lemall.common.entity.UmsResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UmsResourceCategoryService extends IService<UmsResourceCategory>{


    /**
     * 列出所有资源分类
     * @return 资源分类列表
     */
    List<UmsResourceCategoryResponseDTO> listAllResourceCategory();

    /**
     * 添加资源分类
     * @param umsResourceCategoryRequestDTO 参数
     * @return 成功标志
     */
    boolean create(UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO);

    /**
     * 修改资源分类
     * @param id id
     * @param umsResourceCategoryRequestDTO 参数
     * @return 成功标志
     */
    boolean updateRes(Long id, UmsResourceCategoryRequestDTO umsResourceCategoryRequestDTO);

    /**
     * 删除资源分类
     * @param id id
     * @return 成功标志
     */
    boolean delete(Long id);
}
