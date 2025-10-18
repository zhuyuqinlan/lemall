package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeNewProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeNewProductResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeNewProduct;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeNewProductMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeNewProductService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SmsHomeNewProductServiceImpl extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> implements SmsHomeNewProductService{

    @Override
    public boolean create(List<SmsHomeNewProductRequestDTO> homeBrandList) {
        List<SmsHomeNewProduct> smsHomeNewProducts = homeBrandList.stream().map(e -> {
            SmsHomeNewProduct smsHomeNewProduct = new SmsHomeNewProduct();
            BeanUtils.copyProperties(e, smsHomeNewProduct);
            smsHomeNewProduct.setRecommendStatus(1);
            smsHomeNewProduct.setSort(0);
            return smsHomeNewProduct;
        }).toList();
        return super.saveBatch(smsHomeNewProducts);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        return super.update(Wrappers.<SmsHomeNewProduct>lambdaUpdate()
                .eq(SmsHomeNewProduct::getId, id)
                .set(SmsHomeNewProduct::getSort, sort)
        );
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return super.update(Wrappers.<SmsHomeNewProduct>lambdaUpdate()
                .in(SmsHomeNewProduct::getId, ids)
                .set(SmsHomeNewProduct::getRecommendStatus, recommendStatus)
        );
    }

    @Override
    public IPage<SmsHomeNewProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum,pageSize),Wrappers.<SmsHomeNewProduct>lambdaQuery()
                .like(StringUtils.hasText(productName),SmsHomeNewProduct::getProductName,productName)
                .eq(recommendStatus != null,SmsHomeNewProduct::getRecommendStatus,recommendStatus)
                .orderByDesc(SmsHomeNewProduct::getSort)
        ).convert(e -> {
            SmsHomeNewProductResponseDTO responseDTO = new SmsHomeNewProductResponseDTO();
            BeanUtils.copyProperties(e,responseDTO);
            return responseDTO;
        });
    }
}
