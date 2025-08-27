package org.nanguo.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.product.dto.request.PmsProductQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.product.dto.request.PmsProductParamRequestDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsProductParamResultResponseDTO;
import org.nanguo.lemall.business.admin.product.dto.response.PmsProductResponseDTO;
import org.nanguo.lemall.common.entity.PmsProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsProductService extends IService<PmsProduct>{


    /**
     * 分页查询商品
     * @param productQueryParam 查询参数
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<PmsProductResponseDTO> getList(PmsProductQueryParamRequestDTO productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 创建商品
     * @param requestDTO 请求参数
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean create(PmsProductParamRequestDTO requestDTO);

    /**
     * 根据商品id获取商品编辑信息
     * @param id id
     * @return 结果
     */
    PmsProductParamResultResponseDTO getUpdateInfo(Long id);

    /**
     * 更新商品
     * @param id id
     * @param requestDTO 参数
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateProduct(Long id, PmsProductParamRequestDTO requestDTO);

    /**
     * 根据商品名称或货号模糊查询
     * @param keyword 关键词
     * @return 结果
     */
    List<PmsProductResponseDTO> getSimpleList(String keyword);

    /**
     * 批量修改审核状态
     * @param ids ids
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量上下架
     * @param ids ids
     * @param publishStatus 上下架状态
     * @return 成功标志
     */
    boolean updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量推荐商品
     * @param ids ids
     * @param recommendStatus 推荐状态
     * @return 成功标志
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量设为新品
     * @param ids ids
     * @param newStatus 新品状态
     * @return 成功标志
     */
    boolean updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量修改删除状态
     * @param ids ids
     * @param deleteStatus 删除状态
     * @return 成功标志
     */
    boolean updateDeleteStatus(List<Long> ids, Integer deleteStatus);
}
