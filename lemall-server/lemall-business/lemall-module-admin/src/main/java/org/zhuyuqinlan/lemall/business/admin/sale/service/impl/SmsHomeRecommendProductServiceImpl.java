package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeRecommendProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeRecommendProductResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeRecommendProductMapper;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeRecommendProduct;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeRecommendProductService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SmsHomeRecommendProductServiceImpl extends ServiceImpl<SmsHomeRecommendProductMapper, SmsHomeRecommendProduct> implements SmsHomeRecommendProductService{

    @Override
    public boolean create(List<SmsHomeRecommendProductRequestDTO> homeBrandList) {
        List<SmsHomeRecommendProduct> smsHomeRecommendProducts = homeBrandList.stream().map(e -> {
            SmsHomeRecommendProduct smsHomeRecommendProduct = new SmsHomeRecommendProduct();
            smsHomeRecommendProduct.setRecommendStatus(1);
            smsHomeRecommendProduct.setSort(0);
            return smsHomeRecommendProduct;
        }).toList();
        return super.saveBatch(smsHomeRecommendProducts);
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        return super.update(Wrappers.<SmsHomeRecommendProduct>lambdaUpdate()
                .eq(SmsHomeRecommendProduct::getId, id)
                .set(SmsHomeRecommendProduct::getSort, sort)
        );
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return super.update(Wrappers.<SmsHomeRecommendProduct>lambdaUpdate()
                .in(SmsHomeRecommendProduct::getId, ids)
                .set(SmsHomeRecommendProduct::getRecommendStatus, recommendStatus)
        );
    }

    @Override
    public IPage<SmsHomeRecommendProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum,pageSize),Wrappers.<SmsHomeRecommendProduct>lambdaQuery()
                .like(StringUtils.hasText(productName),SmsHomeRecommendProduct::getProductName,productName)
                .eq(recommendStatus != null,SmsHomeRecommendProduct::getRecommendStatus,recommendStatus)
        ).convert(e -> {
            SmsHomeRecommendProductResponseDTO dto = new SmsHomeRecommendProductResponseDTO();
            BeanUtils.copyProperties(e,dto);
            return dto;
        });
    }
}
