package org.zhuyuqinlan.lemall.business.admin.order.service.impl;

import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsCompanyAddressResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.OmsCompanyAddress;
import org.zhuyuqinlan.lemall.business.admin.order.mapper.OmsCompanyAddressMapper;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsCompanyAddressService;

import java.util.List;

@Service
public class OmsCompanyAddressServiceImpl extends ServiceImpl<OmsCompanyAddressMapper, OmsCompanyAddress> implements OmsCompanyAddressService{

    @Override
    public List<OmsCompanyAddressResponseDTO> listAll() {
        return super.list().stream().map(e -> {
            OmsCompanyAddressResponseDTO dto = new OmsCompanyAddressResponseDTO();
            BeanUtils.copyProperties(e,dto);
            return dto;
        }).toList();
    }
}
