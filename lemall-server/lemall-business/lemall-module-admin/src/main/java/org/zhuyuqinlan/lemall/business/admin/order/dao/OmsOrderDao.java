package org.zhuyuqinlan.lemall.business.admin.order.dao;

import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderDetailDTO;

import java.util.List;

public interface OmsOrderDao {
    /**
     * 批量修改库存
     * @param deliveryParamList 参数
     */
    void delivery(@Param("list") List<OmsOrderDeliveryParamRequestDTO> deliveryParamList);

    /**
     * 查询详细信息
     * @param id id
     * @return 结果
     */
    OmsOrderDetailDTO getDetail(Long id);
}
