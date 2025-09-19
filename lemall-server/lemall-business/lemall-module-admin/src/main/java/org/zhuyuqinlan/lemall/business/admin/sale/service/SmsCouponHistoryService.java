package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponHistoryResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponHistory;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SmsCouponHistoryService extends IService<SmsCouponHistory>{


    /**
     * 根据优惠券id，使用状态，订单编号分页获取领取记录
     * @param couponId 优惠价id
     * @param useStatus 使用状态
     * @param orderSn 订单编号
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<SmsCouponHistoryResponseDTO> listPage(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
