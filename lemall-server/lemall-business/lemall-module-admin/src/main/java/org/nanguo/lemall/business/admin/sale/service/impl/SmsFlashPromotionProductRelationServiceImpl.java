package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsFlashPromotionProductRelationRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsFlashPromotionProductRelation;
import org.nanguo.lemall.business.admin.sale.mapper.SmsFlashPromotionProductRelationMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsFlashPromotionProductRelationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsFlashPromotionProductRelationServiceImpl extends ServiceImpl<SmsFlashPromotionProductRelationMapper, SmsFlashPromotionProductRelation> implements SmsFlashPromotionProductRelationService {

    @Override
    public boolean create(List<SmsFlashPromotionProductRelationRequestDTO> relationList) {
        List<SmsFlashPromotionProductRelation> relationPoList = new ArrayList<>();
        for (SmsFlashPromotionProductRelationRequestDTO relation : relationList) {
            SmsFlashPromotionProductRelation relationPo = new SmsFlashPromotionProductRelation();
            BeanUtils.copyProperties(relation, relationPo);
            relationPoList.add(relationPo);
        }
        return super.saveBatch(relationPoList);
    }

    @Override
    public boolean updatePro(Long id, SmsFlashPromotionProductRelationRequestDTO relation) {
        SmsFlashPromotionProductRelation relationPo = new SmsFlashPromotionProductRelation();
        BeanUtils.copyProperties(relation, relationPo);
        relationPo.setId(id);
        return super.updateById(relationPo);
    }

    @Override
    public boolean delete(Long id) {
        return super.removeById(id);
    }

    @Override
    public SmsFlashPromotionProductRelationResponseDTO getItem(Long id) {
        SmsFlashPromotionProductRelationResponseDTO responseDTO = new SmsFlashPromotionProductRelationResponseDTO();
        SmsFlashPromotionProductRelation relation = getById(id);
        BeanUtils.copyProperties(relation, responseDTO);
        return responseDTO;
    }

    @Override
    public IPage<SmsFlashPromotionProductResponseDTO> listPage(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        return baseMapper.getList(new Page<>(pageNum, pageSize), flashPromotionId, flashPromotionSessionId);
    }

    @Override
    public long getCount(Long flashPromotionId, Long id) {
        return super.count(Wrappers.<SmsFlashPromotionProductRelation>lambdaQuery()
                .eq(SmsFlashPromotionProductRelation::getFlashPromotionId, flashPromotionId)
                .eq(SmsFlashPromotionProductRelation::getId, id)
        );
    }
}
