package org.zhuyuqinlan.lemall.business.admin.order.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderSettingRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsOrderSettingDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderSetting;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderSettingMapper;

@Service
public class OmsOrderSettingService extends ServiceImpl<OmsOrderSettingMapper, OmsOrderSetting> {

    public OmsOrderSettingDTO getItem(Long id) {
        OmsOrderSettingDTO responseDTO = new OmsOrderSettingDTO();
        OmsOrderSetting omsOrderSetting = baseMapper.selectById(id);
        BeanUtils.copyProperties(omsOrderSetting, responseDTO);
        return responseDTO;
    }

    public boolean updateSetting(Long id, OmsOrderSettingRequestDTO orderSetting) {
        OmsOrderSetting omsOrderSetting = new OmsOrderSetting();
        BeanUtils.copyProperties(orderSetting, omsOrderSetting);
        omsOrderSetting.setId(id);
        return super.updateById(omsOrderSetting);
    }
}
