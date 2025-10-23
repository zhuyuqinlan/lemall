package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionSessionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsFlashPromotionSessionDetailDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsFlashPromotionSessionDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsFlashPromotionSession;
import org.zhuyuqinlan.lemall.common.mapper.SmsFlashPromotionSessionMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀活动场次管理 Service
 */
@Service
@RequiredArgsConstructor
public class SmsFlashPromotionSessionService extends ServiceImpl<SmsFlashPromotionSessionMapper, SmsFlashPromotionSession> {

    private final SmsFlashPromotionProductRelationService relationService;

    /**
     * 添加场次
     * @param promotionSession 请求参数
     * @return 成功标志
     */
    public boolean create(SmsFlashPromotionSessionRequestDTO promotionSession) {
        SmsFlashPromotionSession smsFlashPromotionSession = new SmsFlashPromotionSession();
        BeanUtils.copyProperties(promotionSession, smsFlashPromotionSession);
        return save(smsFlashPromotionSession);
    }

    /**
     * 修改场次
     * @param id id
     * @param promotionSession 请求参数
     * @return 成功标志
     */
    public boolean updateFlash(Long id, SmsFlashPromotionSessionRequestDTO promotionSession) {
        SmsFlashPromotionSession smsFlashPromotionSession = new SmsFlashPromotionSession();
        BeanUtils.copyProperties(promotionSession, smsFlashPromotionSession);
        smsFlashPromotionSession.setId(id);
        return updateById(smsFlashPromotionSession);
    }

    /**
     * 修改启用状态
     * @param id id
     * @param status 修改状态
     * @return 成功标志
     */
    public boolean updateStatus(Long id, Integer status) {
        return update(Wrappers.<SmsFlashPromotionSession>lambdaUpdate()
                .eq(SmsFlashPromotionSession::getId, id)
                .set(SmsFlashPromotionSession::getStatus, status)
        );
    }

    /**
     * 删除场次
     * @param id id
     * @return 成功标志
     */
    public boolean delete(Long id) {
        return removeById(id);
    }

    /**
     * 获取场次详情
     * @param id id
     * @return 结果
     */
    public SmsFlashPromotionSessionDTO getItem(Long id) {
        SmsFlashPromotionSessionDTO responseDTO = new SmsFlashPromotionSessionDTO();
        SmsFlashPromotionSession smsFlashPromotionSession = getById(id);
        BeanUtils.copyProperties(smsFlashPromotionSession, responseDTO);
        return responseDTO;
    }

    /**
     * 获取全部场次
     * @return 结果
     */
    public List<SmsFlashPromotionSessionDTO> listAll() {
        return list().stream().map(e -> {
            SmsFlashPromotionSessionDTO responseDTO = new SmsFlashPromotionSessionDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        }).toList();
    }

    /**
     * 获取全部可选场次及其数量
     * @param flashPromotionId id
     * @return 结果
     */
    public List<SmsFlashPromotionSessionDetailDTO> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSession> list = list(Wrappers.<SmsFlashPromotionSession>lambdaQuery()
                .eq(SmsFlashPromotionSession::getStatus, 1)
        );
        List<SmsFlashPromotionSessionDetailDTO> result = new ArrayList<>();
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetailDTO detail = new SmsFlashPromotionSessionDetailDTO();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
