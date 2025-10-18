package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeAdvertiseRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeAdvertiseResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeAdvertise;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeAdvertiseMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsHomeAdvertiseService;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements SmsHomeAdvertiseService {

    @Override
    public boolean create(SmsHomeAdvertiseRequestDTO advertise) {
        SmsHomeAdvertise smsHomeAdvertise = new SmsHomeAdvertise();
        BeanUtils.copyProperties(advertise, smsHomeAdvertise);
        smsHomeAdvertise.setClickCount(0);
        smsHomeAdvertise.setOrderCount(0);
        return super.save(smsHomeAdvertise);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return super.update(Wrappers.<SmsHomeAdvertise>lambdaUpdate()
                .eq(SmsHomeAdvertise::getId, id)
                .set(SmsHomeAdvertise::getStatus, status)
        );
    }

    @Override
    public SmsHomeAdvertiseResponseDTO getItem(Long id) {
        SmsHomeAdvertiseResponseDTO responseDTO = new SmsHomeAdvertiseResponseDTO();
        SmsHomeAdvertise smsHomeAdvertise = super.getById(id);
        BeanUtils.copyProperties(smsHomeAdvertise, responseDTO);
        return responseDTO;
    }

    @Override
    public boolean updateHomeAdvertise(Long id, SmsHomeAdvertiseRequestDTO advertise) {
        SmsHomeAdvertise smsHomeAdvertise = new SmsHomeAdvertise();
        BeanUtils.copyProperties(advertise, smsHomeAdvertise);
        smsHomeAdvertise.setId(id);
        return super.updateById(smsHomeAdvertise);
    }

    @Override
    public IPage<SmsHomeAdvertiseResponseDTO> listPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize),
                Wrappers.<SmsHomeAdvertise>lambdaQuery()
                        .like(StringUtils.hasText(name), SmsHomeAdvertise::getName, name)
                        .eq(type != null, SmsHomeAdvertise::getType, type)
                        .between(StringUtils.hasText(endTime),
                                SmsHomeAdvertise::getEndTime,
                                LocalDate.parse(endTime).atStartOfDay(),
                                LocalDate.parse(endTime).atTime(LocalTime.MAX))
                        .orderByDesc(SmsHomeAdvertise::getSort)
        ).convert(e -> {
            SmsHomeAdvertiseResponseDTO dto = new SmsHomeAdvertiseResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
