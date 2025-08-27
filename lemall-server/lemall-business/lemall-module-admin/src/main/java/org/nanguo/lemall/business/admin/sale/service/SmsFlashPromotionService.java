package org.nanguo.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionResponseDTO;
import org.nanguo.lemall.common.entity.SmsFlashPromotion;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SmsFlashPromotionService extends IService<SmsFlashPromotion>{


    /**
     * 添加活动
     * @param flashPromotion 请求参数
     * @return 成功标志
     */
    boolean create(SmsFlashPromotionRequestDTO flashPromotion);

    /**
     * 编辑活动信息
     * @param id id
     * @param flashPromotion 请求参数
     * @return 成功标志
     */
    boolean updateFlash(Long id, SmsFlashPromotionRequestDTO flashPromotion);

    /**
     * 删除活动信息
     * @param id id
     * @return 成功标志
     */
    boolean delete(Long id);

    /**
     * 修改上下线状态
     * @param id id
     * @param status 状态
     * @return 成功标志
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 获取活动详情
     * @param id id
     * @return 结果
     */
    SmsFlashPromotionResponseDTO getItem(Long id);

    /**
     * 根据活动名称分页查询
     * @param keyword 关键词
     * @param pageSize
     * @param pageNum
     * @return
     */
    IPage<SmsFlashPromotionResponseDTO> listPage(String keyword, Integer pageSize, Integer pageNum);
}
