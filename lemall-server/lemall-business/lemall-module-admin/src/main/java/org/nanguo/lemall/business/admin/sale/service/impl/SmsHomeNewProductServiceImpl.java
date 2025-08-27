package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeNewProductRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeNewProductResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsHomeNewProduct;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeNewProductMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeNewProductService;

import java.util.List;

@Service
public class SmsHomeNewProductServiceImpl extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> implements SmsHomeNewProductService{

    @Override
    public boolean create(List<SmsHomeNewProductRequestDTO> homeBrandList) {
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
    public IPage<SmsHomeNewProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return null;
    }
}
