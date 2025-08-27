package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeRecommendSubjectRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeRecommendSubjectResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeRecommendSubjectMapper;
import org.nanguo.lemall.common.entity.SmsHomeRecommendSubject;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeRecommendSubjectService;

import java.util.List;

@Service
public class SmsHomeRecommendSubjectServiceImpl extends ServiceImpl<SmsHomeRecommendSubjectMapper, SmsHomeRecommendSubject> implements SmsHomeRecommendSubjectService{

    @Override
    public boolean create(List<SmsHomeRecommendSubjectRequestDTO> homeBrandList) {
        return false;
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        return false;
    }

    @Override
    public boolean delete(List<Long> ids) {
        return false;
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return false;
    }

    @Override
    public IPage<SmsHomeRecommendSubjectResponseDTO> listPage(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return null;
    }
}
