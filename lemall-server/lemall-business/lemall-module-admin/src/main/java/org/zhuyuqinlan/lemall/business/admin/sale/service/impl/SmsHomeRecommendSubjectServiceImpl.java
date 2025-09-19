package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeRecommendSubjectRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeRecommendSubjectResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.sale.mapper.SmsHomeRecommendSubjectMapper;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeRecommendSubject;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeRecommendSubjectService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SmsHomeRecommendSubjectServiceImpl extends ServiceImpl<SmsHomeRecommendSubjectMapper, SmsHomeRecommendSubject> implements SmsHomeRecommendSubjectService{

    @Override
    public boolean create(List<SmsHomeRecommendSubjectRequestDTO> homeBrandList) {
        List<SmsHomeRecommendSubject> smsHomeRecommendSubjects = homeBrandList.stream().map(e -> {
            SmsHomeRecommendSubject smsHomeRecommendSubject = new SmsHomeRecommendSubject();
            BeanUtils.copyProperties(e, smsHomeRecommendSubject);
            smsHomeRecommendSubject.setRecommendStatus(1);
            smsHomeRecommendSubject.setSort(0);
            return smsHomeRecommendSubject;
        }).toList();
        return super.saveBatch(smsHomeRecommendSubjects);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        return super.update(Wrappers.<SmsHomeRecommendSubject>lambdaUpdate()
                .eq(SmsHomeRecommendSubject::getId, id)
                .set(SmsHomeRecommendSubject::getSort, sort)
        );
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return super.update(Wrappers.<SmsHomeRecommendSubject>lambdaUpdate()
                .in(SmsHomeRecommendSubject::getId, ids)
                .set(SmsHomeRecommendSubject::getRecommendStatus, recommendStatus)
        );
    }

    @Override
    public IPage<SmsHomeRecommendSubjectResponseDTO> listPage(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeRecommendSubject>lambdaQuery()
                .like(StringUtils.hasText(subjectName),SmsHomeRecommendSubject::getSubjectName,subjectName)
                .eq(recommendStatus != null,SmsHomeRecommendSubject::getRecommendStatus,recommendStatus)
                .orderByDesc(SmsHomeRecommendSubject::getSort)
        ).convert(e -> {
            SmsHomeRecommendSubjectResponseDTO responseDTO = new SmsHomeRecommendSubjectResponseDTO();
            BeanUtils.copyProperties(e,responseDTO);
            return responseDTO;
        });
    }
}
