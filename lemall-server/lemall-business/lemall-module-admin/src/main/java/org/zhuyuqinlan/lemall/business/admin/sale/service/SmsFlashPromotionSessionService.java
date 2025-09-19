package org.zhuyuqinlan.lemall.business.admin.sale.service;

import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionSessionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionDetailResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsFlashPromotionSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SmsFlashPromotionSessionService extends IService<SmsFlashPromotionSession>{


    /**
     * 添加场次
     * @param promotionSession 请求参数
     * @return 成功标志
     */
    boolean create(SmsFlashPromotionSessionRequestDTO promotionSession);

    /**
     * 修改场次
     * @param id id
     * @param promotionSession 请求参数
     * @return 成功标志
     */
    boolean updateFlash(Long id, SmsFlashPromotionSessionRequestDTO promotionSession);

    /**
     * 修改启用状态
     * @param id id
     * @param status 修改状态
     * @return 成功标志
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 删除场次
     * @param id id
     * @return 成功标志
     */
    boolean delete(Long id);

    /**
     * 获取场次详情
     * @param id id
     * @return 结果
     */
    SmsFlashPromotionSessionResponseDTO getItem(Long id);

    /**
     * 获取全部场次
     * @return 结果
     */
    List<SmsFlashPromotionSessionResponseDTO> listAll();

    /**
     * 获取全部可选场次及其数量
     * @param flashPromotionId id
     * @return 结果
     */
    List<SmsFlashPromotionSessionDetailResponseDTO> selectList(Long flashPromotionId);
}
