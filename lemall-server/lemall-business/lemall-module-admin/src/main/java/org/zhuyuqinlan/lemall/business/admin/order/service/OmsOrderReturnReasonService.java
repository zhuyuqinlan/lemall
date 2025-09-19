package org.zhuyuqinlan.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderReturnReasonRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnReasonResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderReturnReason;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OmsOrderReturnReasonService extends IService<OmsOrderReturnReason>{


    /**
     * 添加退货原因
     * @param returnReason 参数
     * @return 成功标志
     */
    boolean create(OmsOrderReturnReasonRequestDTO returnReason);

    /**
     * 修改退货原因
     * @param id id
     * @param returnReason 请求参数
     * @return 成功标志
     */
    boolean updateReason(Long id, OmsOrderReturnReasonRequestDTO returnReason);

    /**
     * 批量删除退货原因
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 分页查询全部退货原因
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 成功标志
     */
    IPage<OmsOrderReturnReasonResponseDTO> listPage(Integer pageSize, Integer pageNum);

    /**
     * 获取单个退货原因详情信息
     * @param id id
     * @return 结果
     */
    OmsOrderReturnReasonResponseDTO getItem(Long id);

    /**
     * 修改退货原因启用状态
     * @param ids ids
     * @param status 状态
     * @return 成功标志
     */
    boolean updateStatus(List<Long> ids, Integer status);
}
