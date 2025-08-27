package org.nanguo.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeAdvertiseRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeAdvertiseResponseDTO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.common.entity.SmsHomeAdvertise;
import org.nanguo.lemall.business.admin.sale.mapper.SmsHomeAdvertiseMapper;
import org.nanguo.lemall.business.admin.sale.service.SmsHomeAdvertiseService;

import java.util.List;

@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements SmsHomeAdvertiseService{

    @Override
    public boolean create(SmsHomeAdvertiseRequestDTO advertise) {
        return false;
    }

    @Override
    public boolean delete(List<Long> ids) {
        return false;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return false;
    }

    @Override
    public SmsHomeAdvertiseResponseDTO getItem(Long id) {
        return null;
    }

    @Override
    public boolean updateHomeAdvertise(Long id, SmsHomeAdvertiseRequestDTO advertise) {
        return false;
    }

    @Override
    public IPage<SmsHomeAdvertiseResponseDTO> listPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return null;
    }
}
