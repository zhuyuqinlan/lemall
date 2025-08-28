package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsFlashPromotion;
import org.nanguo.lemall.business.admin.sale.mapper.SmsFlashPromotionMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionService;
import org.springframework.util.StringUtils;

@Service
public class SmsFlashPromotionServiceImpl extends ServiceImpl<SmsFlashPromotionMapper, SmsFlashPromotion> implements SmsFlashPromotionService{

    @Override
    public boolean create(SmsFlashPromotionRequestDTO flashPromotion) {
        SmsFlashPromotion smsFlashPromotion = new SmsFlashPromotion();
        BeanUtils.copyProperties(flashPromotion, smsFlashPromotion);
        return super.save(smsFlashPromotion);
    }

    @Override
    public boolean updateFlash(Long id, SmsFlashPromotionRequestDTO flashPromotion) {
        SmsFlashPromotion smsFlashPromotion = new SmsFlashPromotion();
        BeanUtils.copyProperties(flashPromotion, smsFlashPromotion);
        smsFlashPromotion.setId(id);
        return super.updateById(smsFlashPromotion);
    }

    @Override
    public boolean delete(Long id) {
        return super.removeById(id);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return super.update(Wrappers.<SmsFlashPromotion>lambdaUpdate()
                .eq(SmsFlashPromotion::getId, id)
                .set(SmsFlashPromotion::getStatus, status)
        );
    }

    @Override
    public SmsFlashPromotionResponseDTO getItem(Long id) {
        SmsFlashPromotion smsFlashPromotion = super.getById(id);
        SmsFlashPromotionResponseDTO responseDTO = new SmsFlashPromotionResponseDTO();
        BeanUtils.copyProperties(smsFlashPromotion, responseDTO);
        return responseDTO;
    }

    @Override
    public IPage<SmsFlashPromotionResponseDTO> listPage(String keyword, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum,pageSize),Wrappers.<SmsFlashPromotion>lambdaQuery()
                .like(StringUtils.hasText(keyword),SmsFlashPromotion::getTitle,keyword)
        ).convert(e -> {
            SmsFlashPromotionResponseDTO responseDTO = new SmsFlashPromotionResponseDTO();
            BeanUtils.copyProperties(e,responseDTO);
            return responseDTO;
        });
    }
}
