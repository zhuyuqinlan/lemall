package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeRecommendProductRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeRecommendProductResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeRecommendProductMapper;
import org.nanguo.lemall.common.entity.SmsHomeRecommendProduct;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeRecommendProductService;

import java.util.List;

@Service
public class SmsHomeRecommendProductServiceImpl extends ServiceImpl<SmsHomeRecommendProductMapper, SmsHomeRecommendProduct> implements SmsHomeRecommendProductService{

    @Override
    public boolean create(List<SmsHomeRecommendProductRequestDTO> homeBrandList) {
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
    public IPage<SmsHomeRecommendProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return null;
    }
}
