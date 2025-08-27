package org.nanguo.lemall.business.admin.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderDeliveryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderDetailResponseDTO;
import org.nanguo.lemall.common.entity.OmsOrder;

import java.util.List;

public interface OmsOrderMapper extends BaseMapper<OmsOrder> {
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
    OmsOrderDetailResponseDTO getDetail(Long id);
}