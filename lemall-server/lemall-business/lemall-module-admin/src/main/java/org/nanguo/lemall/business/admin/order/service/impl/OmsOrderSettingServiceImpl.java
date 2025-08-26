package org.nanguo.lemall.business.admin.order.service.impl;

import org.nanguo.lemall.business.admin.order.dto.request.OmsOrderSettingRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderSettingResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.order.mapper.OmsOrderSettingMapper;
import org.nanguo.lemall.business.admin.order.entity.OmsOrderSetting;
import org.nanguo.lemall.business.admin.order.service.OmsOrderSettingService;
@Service
public class OmsOrderSettingServiceImpl extends ServiceImpl<OmsOrderSettingMapper, OmsOrderSetting> implements OmsOrderSettingService{

    @Override
    public OmsOrderSettingResponseDTO getItem(Long id) {
        OmsOrderSettingResponseDTO responseDTO = new OmsOrderSettingResponseDTO();
        OmsOrderSetting omsOrderSetting = baseMapper.selectById(id);
        BeanUtils.copyProperties(omsOrderSetting,responseDTO);
        return responseDTO;
    }

    @Override
    public boolean updateSetting(Long id, OmsOrderSettingRequestDTO orderSetting) {
        OmsOrderSetting omsOrderSetting = new OmsOrderSetting();
        BeanUtils.copyProperties(orderSetting,omsOrderSetting);
        omsOrderSetting.setId(id);
        return super.updateById(omsOrderSetting);
    }
}
