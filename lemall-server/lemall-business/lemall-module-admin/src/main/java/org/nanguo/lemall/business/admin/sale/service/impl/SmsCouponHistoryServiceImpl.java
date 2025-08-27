package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsCouponHistoryResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsCouponHistory;
import org.nanguo.lemall.business.admin.sale.mapper.SmsCouponHistoryMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsCouponHistoryService;
@Service
public class SmsCouponHistoryServiceImpl extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory> implements SmsCouponHistoryService{

    @Override
    public IPage<SmsCouponHistoryResponseDTO> listPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        return null;
    }
}
