package org.zhuyuqinlan.lemall.business.admin.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsOrderReturnReasonRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnReasonResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderReturnReasonMapper;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderReturnReason;
import org.zhuyuqinlan.lemall.business.admin.order.service.OmsOrderReturnReasonService;

import java.util.List;

@Service
public class OmsOrderReturnReasonServiceImpl extends ServiceImpl<OmsOrderReturnReasonMapper, OmsOrderReturnReason> implements OmsOrderReturnReasonService{

    @Override
    public boolean create(OmsOrderReturnReasonRequestDTO returnReason) {
        OmsOrderReturnReason omsOrderReturnReason = new OmsOrderReturnReason();
        BeanUtils.copyProperties(returnReason, omsOrderReturnReason);
        return super.save(omsOrderReturnReason);
    }

    @Override
    public boolean updateReason(Long id, OmsOrderReturnReasonRequestDTO returnReason) {
        OmsOrderReturnReason omsOrderReturnReason = new OmsOrderReturnReason();
        BeanUtils.copyProperties(returnReason, omsOrderReturnReason);
        omsOrderReturnReason.setId(id);
        return super.updateById(omsOrderReturnReason);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public IPage<OmsOrderReturnReasonResponseDTO> listPage(Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize)).convert(e -> {
            OmsOrderReturnReasonResponseDTO re = new OmsOrderReturnReasonResponseDTO();
            BeanUtils.copyProperties(e, re);
            return re;
        });
    }

    @Override
    public OmsOrderReturnReasonResponseDTO getItem(Long id) {
        OmsOrderReturnReason omsOrderReturnReason = super.getById(id);
        OmsOrderReturnReasonResponseDTO re = new OmsOrderReturnReasonResponseDTO();
        BeanUtils.copyProperties(omsOrderReturnReason, re);
        return re;
    }

    @Override
    public boolean updateStatus(List<Long> ids, Integer status) {
        if(!status.equals(0)&&!status.equals(1)) {
            return false;
        }
        return super.update(Wrappers.<OmsOrderReturnReason>lambdaUpdate()
                .in(OmsOrderReturnReason::getId, ids)
                .set(OmsOrderReturnReason::getStatus, status)
        );
    }
}
