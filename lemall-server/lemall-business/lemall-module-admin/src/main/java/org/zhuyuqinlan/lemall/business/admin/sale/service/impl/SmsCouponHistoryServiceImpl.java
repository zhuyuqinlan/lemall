package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponHistoryResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponHistory;
import org.zhuyuqinlan.lemall.business.admin.sale.mapper.SmsCouponHistoryMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponHistoryService;
import org.springframework.util.StringUtils;

@Service
public class SmsCouponHistoryServiceImpl extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory> implements SmsCouponHistoryService{

    @Override
    public IPage<SmsCouponHistoryResponseDTO> listPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum,pageSize), Wrappers.<SmsCouponHistory>lambdaQuery()
                .eq(couponId != null,SmsCouponHistory::getCouponId,couponId)
                .eq(useStatus != null,SmsCouponHistory::getUseStatus,useStatus)
                .eq(StringUtils.hasText(orderSn),SmsCouponHistory::getOrderSn,orderSn)
        ).convert(e -> {
            SmsCouponHistoryResponseDTO dto = new SmsCouponHistoryResponseDTO();
            BeanUtils.copyProperties(e,dto);
            return dto;
        });
    }
}
