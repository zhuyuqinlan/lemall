package org.zhuyuqinlan.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.order.dto.OmsCompanyAddressDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsCompanyAddress;
import org.zhuyuqinlan.lemall.common.mapper.OmsCompanyAddressMapper;

import java.util.List;

@Service
public class OmsCompanyAddressService extends ServiceImpl<OmsCompanyAddressMapper, OmsCompanyAddress> {

    /**
     * 获取所有收货地址
     * @return 结果
     */
    public List<OmsCompanyAddressDTO> listAll() {
        return super.list().stream().map(e -> {
            OmsCompanyAddressDTO dto = new OmsCompanyAddressDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }
}
