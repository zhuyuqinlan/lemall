package org.nanguo.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.order.dto.request.OmsMoneyInfoParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsReceiverInfoParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderDetailResponseDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderResponseDTO;
import org.nanguo.lemall.business.admin.order.entity.OmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OmsOrderService extends IService<OmsOrder>{


    /**
     * 查询订单
     * @param queryParam 查询参数
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<OmsOrderResponseDTO> listPage(OmsOrderQueryParamRequestDTO queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量发货
     * @param deliveryParamList 批量操作列表
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean delivery(List<OmsOrderDeliveryParamRequestDTO> deliveryParamList);

    /**
     * 批量关闭订单
     * @param ids ids
     * @param note 记录
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean close(List<Long> ids, String note);

    /**
     * 批量删除订单
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 获取订单详情:订单信息、商品信息、操作记录
     * @param id id
     * @return 结果
     */
    OmsOrderDetailResponseDTO detail(Long id);

    /**
     * 修改收货人信息
     * @param receiverInfoParam 请求参数
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateReceiverInfo(OmsReceiverInfoParamRequestDTO receiverInfoParam);

    /**
     * 修改订单费用信息
     * @param moneyInfoParam 请求参数
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateMoneyInfo(OmsMoneyInfoParamRequestDTO moneyInfoParam);

    /**
     * 备注订单
     * @param id id
     * @param note 备注
     * @param status 状态
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateNote(Long id, String note, Integer status);
}
