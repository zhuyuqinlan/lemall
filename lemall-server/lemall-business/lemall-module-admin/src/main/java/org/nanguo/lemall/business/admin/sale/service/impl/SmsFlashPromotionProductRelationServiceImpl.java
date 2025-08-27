package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionProductRelationRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsFlashPromotionProductRelation;
import org.nanguo.lemall.business.admin.sale.mapper.SmsFlashPromotionProductRelationMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionProductRelationService;

import java.util.List;

@Service
public class SmsFlashPromotionProductRelationServiceImpl extends ServiceImpl<SmsFlashPromotionProductRelationMapper, SmsFlashPromotionProductRelation> implements SmsFlashPromotionProductRelationService{

    @Override
    public boolean create(List<SmsFlashPromotionProductRelationRequestDTO> relationList) {
        return false;
    }

    @Override
    public boolean updatePro(Long id, SmsFlashPromotionProductRelationRequestDTO relation) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public SmsFlashPromotionProductRelationResponseDTO getItem(Long id) {
        return null;
    }

    @Override
    public IPage<SmsFlashPromotionProductRelationResponseDTO> listPage(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        return null;
    }
}
