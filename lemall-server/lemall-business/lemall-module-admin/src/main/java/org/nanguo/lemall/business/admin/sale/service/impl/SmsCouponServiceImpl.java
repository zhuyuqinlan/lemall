package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsCouponResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.sale.mapper.SmsCouponMapper;
import org.nanguo.lemall.common.entity.SmsCoupon;
import org.nanguo.lemall.business.admin.sale.service.SmsCouponService;
@Service
public class SmsCouponServiceImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements SmsCouponService{

    @Override
    public boolean create(SmsCouponParamRequestDTO couponParam) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean updateCoupon(Long id, SmsCouponParamRequestDTO couponParam) {
        return false;
    }

    @Override
    public IPage<SmsCouponResponseDTO> listPage(String name, Integer type, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public SmsCouponParamRequestDTO getItem(Long id) {
        return null;
    }
}
