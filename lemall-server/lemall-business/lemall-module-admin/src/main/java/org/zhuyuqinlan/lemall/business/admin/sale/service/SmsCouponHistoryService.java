package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponHistory;
import org.zhuyuqinlan.lemall.common.mapper.SmsCouponHistoryMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponHistoryResponseDTO;


@Service
public class SmsCouponHistoryService extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory> {

    /**
     * 根据优惠券id，使用状态，订单编号分页获取领取记录
     * @param couponId 优惠券id
     * @param useStatus 使用状态
     * @param orderSn 订单编号
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 分页结果
     */
    public IPage<SmsCouponHistoryResponseDTO> listPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<SmsCouponHistory>lambdaQuery()
                        .eq(couponId != null, SmsCouponHistory::getCouponId, couponId)
                        .eq(useStatus != null, SmsCouponHistory::getUseStatus, useStatus)
                        .eq(StringUtils.hasText(orderSn), SmsCouponHistory::getOrderSn, orderSn)
                )
                .convert(e -> {
                    SmsCouponHistoryResponseDTO dto = new SmsCouponHistoryResponseDTO();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                });
    }
}
