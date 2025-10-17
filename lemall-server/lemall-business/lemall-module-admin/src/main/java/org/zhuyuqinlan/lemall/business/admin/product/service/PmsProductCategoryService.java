package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductCategoryRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductCategoryResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductCategoryWithChildrenItem;
import org.zhuyuqinlan.lemall.common.entity.PmsProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsProductCategoryService extends IService<PmsProductCategory>{


    /**
     * 查询所有一级分类及子分类
     * @return 结果
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    /**
     * 添加产品分类
     * @param pmsProductCategoryRequestDTO 请求参数
     * @return 成功标志
     */
    @Transactional
    boolean create(PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO);

    /**
     * 修改商品分类
     * @param id id
     * @param pmsProductCategoryRequestDTO 请求参数
     * @return 成功标志
     */
    @Transactional
    boolean updateCategory(Long id, PmsProductCategoryRequestDTO pmsProductCategoryRequestDTO);

    /**
     * 分页查询商品分类
     * @param parentId 父节点id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 结果
     */
    IPage<PmsProductCategoryResponseDTO> getList(Long parentId, Integer pageNum, Integer pageSize);

    /**
     *根据id获取商品分类
     * @param id id
     * @return 结果
     */
    PmsProductCategoryResponseDTO getItemById(Long id);

    /**
     * 删除商品分类
     * @param id id
     * @return 成功标志
     */
    boolean deleteCategoryById(Long id);

    /**
     * 修改导航栏显示状态
     * @param ids ids
     * @param navStatus 导航栏状态
     * @return 成功标志
     */
    boolean updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 修改显示状态
     * @param ids ids
     * @param showStatus 显示状态
     * @return 成功标志
     */
    boolean updateShowStatus(List<Long> ids, Integer showStatus);
}
