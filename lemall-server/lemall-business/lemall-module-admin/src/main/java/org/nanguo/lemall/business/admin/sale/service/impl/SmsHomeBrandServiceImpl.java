package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeBrandRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeBrandResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeBrandMapper;
import org.nanguo.lemall.common.entity.SmsHomeBrand;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeBrandService;

import java.util.List;

@Service
public class SmsHomeBrandServiceImpl extends ServiceImpl<SmsHomeBrandMapper, SmsHomeBrand> implements SmsHomeBrandService{

    @Override
    public boolean create(List<SmsHomeBrandRequestDTO> homeBrandList) {
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
    public IPage<SmsHomeBrandResponseDTO> listPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return null;
    }
}
