package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeAdvertiseRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeAdvertiseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeAdvertise;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeAdvertiseMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 首页广告管理 Service
 */
@Service
public class SmsHomeAdvertiseService extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> {

    /**
     * 添加广告
     * @param advertise 请求参数
     * @return 成功标志
     */
    public boolean create(SmsHomeAdvertiseRequestDTO advertise) {
        SmsHomeAdvertise smsHomeAdvertise = new SmsHomeAdvertise();
        BeanUtils.copyProperties(advertise, smsHomeAdvertise);
        smsHomeAdvertise.setClickCount(0);
        smsHomeAdvertise.setOrderCount(0);
        return save(smsHomeAdvertise);
    }

    /**
     * 删除广告
     * @param ids ids
     * @return 成功标志
     */
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 修改上下线状态
     * @param id id
     * @param status 上下线状态
     * @return 成功标志
     */
    public boolean updateStatus(Long id, Integer status) {
        return update(Wrappers.<SmsHomeAdvertise>lambdaUpdate()
                .eq(SmsHomeAdvertise::getId, id)
                .set(SmsHomeAdvertise::getStatus, status)
        );
    }

    /**
     * 获取广告详情
     * @param id id
     * @return 结果
     */
    public SmsHomeAdvertiseDTO getItem(Long id) {
        SmsHomeAdvertiseDTO responseDTO = new SmsHomeAdvertiseDTO();
        SmsHomeAdvertise smsHomeAdvertise = getById(id);
        BeanUtils.copyProperties(smsHomeAdvertise, responseDTO);
        return responseDTO;
    }

    /**
     * 修改广告
     * @param id id
     * @param advertise 参数
     * @return 成功标志
     */
    public boolean updateHomeAdvertise(Long id, SmsHomeAdvertiseRequestDTO advertise) {
        SmsHomeAdvertise smsHomeAdvertise = new SmsHomeAdvertise();
        BeanUtils.copyProperties(advertise, smsHomeAdvertise);
        smsHomeAdvertise.setId(id);
        return updateById(smsHomeAdvertise);
    }

    /**
     * 分页查询广告
     * @param name name
     * @param type 类型
     * @param endTime 结束时间
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsHomeAdvertiseDTO> listPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize),
                Wrappers.<SmsHomeAdvertise>lambdaQuery()
                        .like(StringUtils.hasText(name), SmsHomeAdvertise::getName, name)
                        .eq(type != null, SmsHomeAdvertise::getType, type)
                        .between(StringUtils.hasText(endTime),
                                SmsHomeAdvertise::getEndTime,
                                LocalDate.parse(endTime).atStartOfDay(),
                                LocalDate.parse(endTime).atTime(LocalTime.MAX))
                        .orderByDesc(SmsHomeAdvertise::getSort)
        ).convert(e -> {
            SmsHomeAdvertiseDTO dto = new SmsHomeAdvertiseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
