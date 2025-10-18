package org.zhuyuqinlan.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderReturnReasonRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnReasonResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderReturnReason;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderReturnReasonMapper;

import java.util.List;

@Service
public class OmsOrderReturnReasonService extends ServiceImpl<OmsOrderReturnReasonMapper, OmsOrderReturnReason> {

    /**
     * 添加退货原因
     */
    public boolean create(OmsOrderReturnReasonRequestDTO returnReason) {
        OmsOrderReturnReason omsOrderReturnReason = new OmsOrderReturnReason();
        BeanUtils.copyProperties(returnReason, omsOrderReturnReason);
        return super.save(omsOrderReturnReason);
    }

    /**
     * 修改退货原因
     */
    public boolean updateReason(Long id, OmsOrderReturnReasonRequestDTO returnReason) {
        OmsOrderReturnReason omsOrderReturnReason = new OmsOrderReturnReason();
        BeanUtils.copyProperties(returnReason, omsOrderReturnReason);
        omsOrderReturnReason.setId(id);
        return super.updateById(omsOrderReturnReason);
    }

    /**
     * 批量删除退货原因
     */
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    /**
     * 分页查询全部退货原因
     */
    public IPage<OmsOrderReturnReasonResponseDTO> listPage(Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize)).convert(e -> {
            OmsOrderReturnReasonResponseDTO re = new OmsOrderReturnReasonResponseDTO();
            BeanUtils.copyProperties(e, re);
            return re;
        });
    }

    /**
     * 获取单个退货原因详情信息
     */
    public OmsOrderReturnReasonResponseDTO getItem(Long id) {
        OmsOrderReturnReason omsOrderReturnReason = super.getById(id);
        OmsOrderReturnReasonResponseDTO re = new OmsOrderReturnReasonResponseDTO();
        BeanUtils.copyProperties(omsOrderReturnReason, re);
        return re;
    }

    /**
     * 修改退货原因启用状态
     */
    public boolean updateStatus(List<Long> ids, Integer status) {
        if (!status.equals(0) && !status.equals(1)) {
            return false;
        }
        return super.update(Wrappers.<OmsOrderReturnReason>lambdaUpdate()
                .in(OmsOrderReturnReason::getId, ids)
                .set(OmsOrderReturnReason::getStatus, status)
        );
    }
}
