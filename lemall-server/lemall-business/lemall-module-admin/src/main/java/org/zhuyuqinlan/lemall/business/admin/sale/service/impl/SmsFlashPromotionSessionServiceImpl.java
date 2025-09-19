package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionSessionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionDetailResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsFlashPromotionProductRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.sale.mapper.SmsFlashPromotionSessionMapper;
import org.zhuyuqinlan.lemall.common.entity.SmsFlashPromotionSession;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsFlashPromotionSessionService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsFlashPromotionSessionServiceImpl extends ServiceImpl<SmsFlashPromotionSessionMapper, SmsFlashPromotionSession> implements SmsFlashPromotionSessionService {

    private final SmsFlashPromotionProductRelationService relationService;

    @Override
    public boolean create(SmsFlashPromotionSessionRequestDTO promotionSession) {
        SmsFlashPromotionSession smsFlashPromotionSession = new SmsFlashPromotionSession();
        BeanUtils.copyProperties(promotionSession, smsFlashPromotionSession);
        return super.save(smsFlashPromotionSession);
    }

    @Override
    public boolean updateFlash(Long id, SmsFlashPromotionSessionRequestDTO promotionSession) {
        SmsFlashPromotionSession smsFlashPromotionSession = new SmsFlashPromotionSession();
        BeanUtils.copyProperties(promotionSession, smsFlashPromotionSession);
        smsFlashPromotionSession.setId(id);
        return super.updateById(smsFlashPromotionSession);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return super.update(Wrappers.<SmsFlashPromotionSession>lambdaUpdate()
                .eq(SmsFlashPromotionSession::getId, id)
                .set(SmsFlashPromotionSession::getStatus, status)
        );
    }

    @Override
    public boolean delete(Long id) {
        return super.removeById(id);
    }

    @Override
    public SmsFlashPromotionSessionResponseDTO getItem(Long id) {
        SmsFlashPromotionSessionResponseDTO responseDTO = new SmsFlashPromotionSessionResponseDTO();
        SmsFlashPromotionSession smsFlashPromotionSession = getById(id);
        BeanUtils.copyProperties(smsFlashPromotionSession, responseDTO);
        return responseDTO;
    }

    @Override
    public List<SmsFlashPromotionSessionResponseDTO> listAll() {
        return super.list().stream().map(e -> {
            SmsFlashPromotionSessionResponseDTO responseDTO = new SmsFlashPromotionSessionResponseDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        }).toList();
    }

    @Override
    public List<SmsFlashPromotionSessionDetailResponseDTO> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSession> list = super.list(Wrappers.<SmsFlashPromotionSession>lambdaQuery()
                .eq(SmsFlashPromotionSession::getStatus, 1)
        );
        List<SmsFlashPromotionSessionDetailResponseDTO> result = new ArrayList<>();
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetailResponseDTO detail = new SmsFlashPromotionSessionDetailResponseDTO();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
