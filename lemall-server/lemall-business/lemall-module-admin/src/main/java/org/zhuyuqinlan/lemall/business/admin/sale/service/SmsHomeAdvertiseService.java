package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeAdvertiseRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeAdvertiseResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeAdvertise;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SmsHomeAdvertiseService extends IService<SmsHomeAdvertise>{


    /**
     * 添加广告
     * @param advertise 请求参数
     * @return  成功标志
     */
    boolean create(SmsHomeAdvertiseRequestDTO advertise);

    /**
     * 删除广告
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 修改上下线状态
     * @param id id
     * @param status 上下线状态
     * @return 成功标志
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 获取广告详情
     * @param id id
     * @return 结果
     */
    SmsHomeAdvertiseResponseDTO getItem(Long id);

    /**
     * 修改广告
     * @param id id
     * @param advertise 参数
     * @return 成功标志
     */
    boolean updateHomeAdvertise(Long id, SmsHomeAdvertiseRequestDTO advertise);

    /**
     * 分页查询广告
     * @param name name
     * @param type 类型
     * @param endTime 结束时间
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 成功标志
     */
    IPage<SmsHomeAdvertiseResponseDTO> listPage(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
