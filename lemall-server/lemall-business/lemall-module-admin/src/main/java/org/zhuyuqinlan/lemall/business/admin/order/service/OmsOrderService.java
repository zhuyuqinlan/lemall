package org.zhuyuqinlan.lemall.business.admin.order.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.order.dao.OmsOrderDao;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsMoneyInfoParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderQueryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsReceiverInfoParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderDetailResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsOrderOperateHistoryService;
import org.zhuyuqinlan.lemall.common.entity.OmsOrder;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderOperateHistory;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderMapper;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OmsOrderService extends ServiceImpl<OmsOrderMapper, OmsOrder> {

    private final OmsOrderOperateHistoryService orderOperateHistoryService;
    private final OmsOrderDao omsOrderDao;

    public IPage<OmsOrderResponseDTO> listPage(OmsOrderQueryParamRequestDTO queryParam, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<OmsOrder>lambdaQuery()
                .eq(OmsOrder::getDeleteStatus, 0)
                .eq(StringUtils.hasText(queryParam.getOrderSn()), OmsOrder::getOrderSn, queryParam.getOrderSn())
                .eq(queryParam.getStatus() != null, OmsOrder::getStatus, queryParam.getStatus())
                .eq(queryParam.getSourceType() != null, OmsOrder::getSourceType, queryParam.getSourceType())
                .eq(queryParam.getOrderType() != null, OmsOrder::getOrderType, queryParam.getOrderType())
                .like(StringUtils.hasText(queryParam.getCreateTime()), OmsOrder::getCreateTime, queryParam.getCreateTime())
                .and(StringUtils.hasText(queryParam.getReceiverKeyword()), q ->
                        q.like(OmsOrder::getReceiverName, queryParam.getReceiverKeyword())
                                .or()
                                .like(OmsOrder::getReceiverPhone, queryParam.getReceiverKeyword())
                )
        ).convert(e -> {
            OmsOrderResponseDTO responseDTO = new OmsOrderResponseDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        });
    }

    @Transactional
    public boolean delivery(List<OmsOrderDeliveryParamRequestDTO> deliveryParamList) {
        omsOrderDao.delivery(deliveryParamList);
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan(StpUtil.getLoginId().toString());
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).toList();
        return orderOperateHistoryService.saveBatch(operateHistoryList);
    }

    @Transactional
    public boolean close(List<Long> ids, String note) {
        super.update(Wrappers.<OmsOrder>lambdaUpdate()
                .in(OmsOrder::getId, ids)
                .eq(OmsOrder::getDeleteStatus, 0)
                .set(OmsOrder::getStatus, 4)
        );

        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan(StpUtil.getLoginId().toString());
            history.setOrderStatus(4);
            history.setNote("订单关闭:" + note);
            return history;
        }).toList();

        return orderOperateHistoryService.saveBatch(historyList);
    }

    public boolean delete(List<Long> ids) {
        return super.update(Wrappers.<OmsOrder>lambdaUpdate()
                .eq(OmsOrder::getDeleteStatus, 0)
                .in(OmsOrder::getId, ids)
                .set(OmsOrder::getDeleteStatus, 1)
        );
    }

    public OmsOrderDetailResponseDTO detail(Long id) {
        return omsOrderDao.getDetail(id);
    }

    @Transactional
    public boolean updateReceiverInfo(OmsReceiverInfoParamRequestDTO receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        super.updateById(order);

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan(StpUtil.getLoginId().toString());
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        return orderOperateHistoryService.save(history);
    }

    @Transactional
    public boolean updateMoneyInfo(OmsMoneyInfoParamRequestDTO moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        super.updateById(order);

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan(StpUtil.getLoginId().toString());
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        return orderOperateHistoryService.save(history);
    }

    @Transactional
    public boolean updateNote(Long id, String note, Integer status) {
        super.update(Wrappers.<OmsOrder>lambdaUpdate()
                .eq(OmsOrder::getId, id)
                .set(OmsOrder::getNote, note)
                .set(OmsOrder::getModifyTime, new Date())
        );

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan(StpUtil.getLoginId().toString());
        history.setOrderStatus(status);
        history.setNote("修改备注信息：" + note);
        return orderOperateHistoryService.save(history);
    }
}
