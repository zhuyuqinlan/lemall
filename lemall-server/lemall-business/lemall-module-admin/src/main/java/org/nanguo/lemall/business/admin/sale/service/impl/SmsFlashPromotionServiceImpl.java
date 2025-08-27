package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsFlashPromotion;
import org.nanguo.lemall.business.admin.sale.mapper.SmsFlashPromotionMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionService;
@Service
public class SmsFlashPromotionServiceImpl extends ServiceImpl<SmsFlashPromotionMapper, SmsFlashPromotion> implements SmsFlashPromotionService{

    @Override
    public boolean create(SmsFlashPromotionRequestDTO flashPromotion) {
        return false;
    }

    @Override
    public boolean updateFlash(Long id, SmsFlashPromotionRequestDTO flashPromotion) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return false;
    }

    @Override
    public SmsFlashPromotionResponseDTO getItem(Long id) {
        return null;
    }

    @Override
    public IPage<SmsFlashPromotionResponseDTO> listPage(String keyword, Integer pageSize, Integer pageNum) {
        return null;
    }
}
