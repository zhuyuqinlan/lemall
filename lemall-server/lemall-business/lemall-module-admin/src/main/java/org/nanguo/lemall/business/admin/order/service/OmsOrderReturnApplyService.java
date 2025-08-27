package org.nanguo.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.order.dto.request.OmsReturnApplyQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsUpdateStatusParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResponseDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;
import org.nanguo.lemall.common.entity.OmsOrderReturnApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OmsOrderReturnApplyService extends IService<OmsOrderReturnApply>{


    /**
     * 分页查询退货申请
     * @param queryParam 查询参数
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<OmsOrderReturnApplyResponseDTO> listPage(OmsReturnApplyQueryParamRequestDTO queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 获取退货申请详情
     * @param id id
     * @return 结果
     */
    OmsOrderReturnApplyResultResponseDTO getItem(Long id);

    /**
     * 修改申请状态
     * @param id id
     * @param statusParam 请求参数
     * @return 成功标志
     */
    boolean updateStatus(Long id, OmsUpdateStatusParamRequestDTO statusParam);
}
