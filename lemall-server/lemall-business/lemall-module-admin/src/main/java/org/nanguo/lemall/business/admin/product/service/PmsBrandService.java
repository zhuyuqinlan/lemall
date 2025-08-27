package org.nanguo.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.product.dto.request.PmsBrandRequestDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsBrandResponseDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nanguo.lemall.common.entity.PmsBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsBrandService extends IService<PmsBrand>{


    /**
     * 分页查询品牌列表
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 结果
     */
    IPage<PmsBrandResponseDTO> getList(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 获取所有品牌
     * @return 结果
     */
    List<PmsBrandResponseDTO> getListAll();

    /**
     * 添加品牌
     * @param requestDTO 请求参数
     * @return 成功标志
     */
    boolean create(PmsBrandRequestDTO requestDTO);

    /**
     * 更新品牌
     * @param id id
     * @param requestDTO 请求参数
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateBrand(Long id, PmsBrandRequestDTO requestDTO);

    /**
     * 删除品牌
     * @param id id
     * @return 成功标志
     */
    boolean deleteBrandById(Long id);

    /**
     * 根据品牌id查询
     * @param id id
     * @return 结果
     */
    PmsBrandResponseDTO getBrandById(Long id);

    /**
     * 根据id批量删除品牌
     * @param ids ids
     * @return 影响记录条数
     */
    boolean deleteBrandBatch(List<Long> ids);

    /**
     * 批量更新显示状态
     * @param ids ids
     * @param showStatus 状态
     * @return 成功标志
     */
    boolean updateBrandByIds(List<Long> ids, Integer showStatus);

    /**
     * 批量更新厂家制造商状态
     * @param ids ids
     * @param factoryStatus 制造商状态
     * @return 成功标志
     */
    boolean updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
