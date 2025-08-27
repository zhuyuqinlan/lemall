package org.nanguo.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionProductRelationRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.nanguo.lemall.common.entity.SmsFlashPromotionProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SmsFlashPromotionProductRelationService extends IService<SmsFlashPromotionProductRelation>{


    /**
     * 批量选择商品添加关联
     * @param relationList 请求参数列表
     * @return 成功标志
     */
    boolean create(List<SmsFlashPromotionProductRelationRequestDTO> relationList);

    /**
     * 修改关联相关信息
     * @param id id
     * @param relation 请求参数
     * @return 成功标志
     */
    boolean updatePro(Long id, SmsFlashPromotionProductRelationRequestDTO relation);

    /**
     * 删除关联
     * @param id id
     * @return 成功标志
     */
    boolean delete(Long id);

    /**
     * 获取管理商品促销信息
     * @param id id
     * @return 返回结果
     */
    SmsFlashPromotionProductRelationResponseDTO getItem(Long id);

    /**
     * 分页查询不同场次关联及商品信息
     * @param flashPromotionId flashPromotionId
     * @param flashPromotionSessionId flashPromotionSessionId
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<SmsFlashPromotionProductRelationResponseDTO> listPage(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum);
}
