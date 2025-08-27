package org.nanguo.lemall.business.admin.sale.service.impl;

import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionSessionRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionDetailResponseDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionSessionResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsFlashPromotionSessionMapper;
import org.nanguo.lemall.common.entity.SmsFlashPromotionSession;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionSessionService;

import java.util.List;

@Service
public class SmsFlashPromotionSessionServiceImpl extends ServiceImpl<SmsFlashPromotionSessionMapper, SmsFlashPromotionSession> implements SmsFlashPromotionSessionService{

    @Override
    public boolean create(SmsFlashPromotionSessionRequestDTO promotionSession) {
        return false;
    }

    @Override
    public boolean updateFlash(Long id, SmsFlashPromotionSessionRequestDTO promotionSession) {
        return false;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public SmsFlashPromotionSessionResponseDTO getItem(Long id) {
        return null;
    }

    @Override
    public List<SmsFlashPromotionSessionResponseDTO> listAll() {
        return List.of();
    }

    @Override
    public List<SmsFlashPromotionSessionDetailResponseDTO> selectList(Long flashPromotionId) {
        return List.of();
    }
}
