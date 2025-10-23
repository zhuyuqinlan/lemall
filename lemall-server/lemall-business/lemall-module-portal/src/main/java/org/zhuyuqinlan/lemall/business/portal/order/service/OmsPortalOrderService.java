package org.zhuyuqinlan.lemall.business.portal.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.business.portal.order.dto.ConfirmOrderResult;
import org.zhuyuqinlan.lemall.business.portal.order.dto.OmsOrderDetail;
import org.zhuyuqinlan.lemall.business.portal.order.dto.request.OrderParam;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OmsPortalOrderService {

    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        return null;
    }

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        return null;
    }

    /**
     * 支付成功后的回调
     */
    @Transactional
    public Integer paySuccess(Long orderId, Integer payType) {
        return null;
    }

    /**
     * 自动取消超时订单
     */
    @Transactional
    public Integer cancelTimeOutOrder() {
        return null;
    }

    /**
     * 取消单个超时订单
     */
    @Transactional
    public void cancelOrder(Long orderId) {

    }

    /**
     * 发送延迟消息取消订单
     */
    public void sendDelayMessageCancelOrder(Long orderId) {
    }

    /**
     * 确认收货
     */
    public void confirmReceiveOrder(Long orderId) {
    }

    /**
     * 分页获取用户订单
     */
    public IPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        return null;
    }

    /**
     * 根据订单ID获取订单详情
     */
    public OmsOrderDetail detail(Long orderId) {
        return null;
    }

    /**
     * 用户根据订单ID删除订单
     */
    public void deleteOrder(Long orderId) {

    }

    /**
     * 根据orderSn来实现的支付成功逻辑
     */
    @Transactional
    public void paySuccessByOrderSn(String orderSn, Integer payType) {

    }
}
