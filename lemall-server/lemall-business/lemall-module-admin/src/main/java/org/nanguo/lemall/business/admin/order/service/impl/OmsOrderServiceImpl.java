package org.nanguo.lemall.business.admin.order.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nanguo.lemall.business.admin.order.dto.request.OmsMoneyInfoParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsReceiverInfoParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderDetailResponseDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderResponseDTO;
import org.nanguo.lemall.business.admin.order.entity.OmsOrderOperateHistory;
import org.nanguo.lemall.business.admin.order.service.OmsOrderOperateHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.order.entity.OmsOrder;
import org.nanguo.lemall.business.admin.order.mapper.OmsOrderMapper;
import org.nanguo.lemall.business.admin.order.service.OmsOrderService;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    private final OmsOrderOperateHistoryService orderOperateHistoryService;

    @Override
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

    @Override
    public boolean delivery(List<OmsOrderDeliveryParamRequestDTO> deliveryParamList) {
        //批量发货
        baseMapper.delivery(deliveryParamList);
        //添加操作记录
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

    @Override
    public boolean close(List<Long> ids, String note) {
        // 更新状态
        super.update(Wrappers.<OmsOrder>lambdaUpdate()
                .in(OmsOrder::getId, ids)
                .eq(OmsOrder::getDeleteStatus, 0)
                .set(OmsOrder::getStatus,4)
        );
        // 插入记录
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan(StpUtil.getLoginId().toString());
            history.setOrderStatus(4);
            history.setNote("订单关闭:"+note);
            return history;
        }).toList();

        return orderOperateHistoryService.saveBatch(historyList);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.update(Wrappers.<OmsOrder>lambdaUpdate()
                .eq(OmsOrder::getDeleteStatus, 0)
                .in(OmsOrder::getId, ids)
                .set(OmsOrder::getDeleteStatus, 1)
        );
    }

    @Override
    public OmsOrderDetailResponseDTO detail(Long id) {
        return baseMapper.getDetail(id);
    }

    @Override
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
        //插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan(StpUtil.getLoginId().toString());
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        return orderOperateHistoryService.save(history);
    }

    @Override
    public boolean updateMoneyInfo(OmsMoneyInfoParamRequestDTO moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        super.updateById(order);
        // 插入操作记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan(StpUtil.getLoginId().toString());
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        return orderOperateHistoryService.save(history);
    }

    @Override
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
        history.setNote("修改备注信息："+note);
        return orderOperateHistoryService.save(history);
    }
}
