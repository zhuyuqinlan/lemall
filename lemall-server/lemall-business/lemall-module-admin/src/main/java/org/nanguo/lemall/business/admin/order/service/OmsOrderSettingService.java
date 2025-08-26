package org.nanguo.lemall.business.admin.order.service;

import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderSettingRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderSettingResponseDTO;
import org.nanguo.lemall.business.admin.order.entity.OmsOrderSetting;
import com.baomidou.mybatisplus.extension.service.IService;
public interface OmsOrderSettingService extends IService<OmsOrderSetting>{

    /**
     * 获取指定订单设置
     * @param id id
     * @return 结果
     */
    OmsOrderSettingResponseDTO getItem(Long id);

    /**
     * 修改指定订单设置
     * @param id id
     * @param orderSetting 请求参数
     * @return 成功标志
     */
    boolean updateSetting(Long id, OmsOrderSettingRequestDTO orderSetting);
}
