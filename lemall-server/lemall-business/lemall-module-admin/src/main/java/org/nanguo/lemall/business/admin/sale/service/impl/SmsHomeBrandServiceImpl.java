package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeBrandRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeBrandResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeBrandMapper;
import org.nanguo.lemall.common.entity.SmsHomeBrand;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeBrandService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SmsHomeBrandServiceImpl extends ServiceImpl<SmsHomeBrandMapper, SmsHomeBrand> implements SmsHomeBrandService {

    @Override
    public boolean create(List<SmsHomeBrandRequestDTO> homeBrandList) {
        List<SmsHomeBrand> smsHomeBrands = homeBrandList.stream().map(e -> {
            SmsHomeBrand smsHomeBrand = new SmsHomeBrand();
            BeanUtils.copyProperties(e, smsHomeBrand);
            return smsHomeBrand;
        }).toList();
        return super.saveBatch(smsHomeBrands);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        return super.update(Wrappers.<SmsHomeBrand>lambdaUpdate()
                .eq(SmsHomeBrand::getId, id)
                .set(SmsHomeBrand::getSort, sort)
        );
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return super.update(Wrappers.<SmsHomeBrand>lambdaUpdate()
                .in(SmsHomeBrand::getId, ids)
                .eq(SmsHomeBrand::getRecommendStatus, recommendStatus)
        );
    }

    @Override
    public IPage<SmsHomeBrandResponseDTO> listPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeBrand>lambdaQuery()
                .like(StringUtils.hasText(brandName), SmsHomeBrand::getBrandName, brandName)
                .eq(recommendStatus != null, SmsHomeBrand::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeBrand::getSort)
        ).convert(e -> {
            SmsHomeBrandResponseDTO dto = new SmsHomeBrandResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
