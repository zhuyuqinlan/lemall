package org.nanguo.lemall.business.admin.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nanguo.lemall.business.admin.order.dto.request.OmsReturnApplyQueryParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.request.OmsUpdateStatusParamRequestDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResponseDTO;
import org.nanguo.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.order.mapper.OmsOrderReturnApplyMapper;
import org.nanguo.lemall.business.admin.order.entity.OmsOrderReturnApply;
import org.nanguo.lemall.business.admin.order.service.OmsOrderReturnApplyService;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class OmsOrderReturnApplyServiceImpl extends ServiceImpl<OmsOrderReturnApplyMapper, OmsOrderReturnApply> implements OmsOrderReturnApplyService {

    @Override
    public IPage<OmsOrderReturnApplyResponseDTO> listPage(OmsReturnApplyQueryParamRequestDTO queryParam, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<OmsOrderReturnApply>lambdaQuery()
                        .eq(queryParam.getId() != null, OmsOrderReturnApply::getId, queryParam.getId())
                        .eq(queryParam.getStatus() != null, OmsOrderReturnApply::getStatus, queryParam.getStatus())
                        .eq(StringUtils.hasText(queryParam.getHandleMan()), OmsOrderReturnApply::getHandleMan, queryParam.getHandleMan())
                        .like(StringUtils.hasText(queryParam.getCreateTime()), OmsOrderReturnApply::getCreateTime, queryParam.getCreateTime())
                        .like(StringUtils.hasText(queryParam.getHandleTime()), OmsOrderReturnApply::getHandleTime, queryParam.getHandleTime())
                        .and(StringUtils.hasText(queryParam.getReceiverKeyword()), e -> {
                            e.like(OmsOrderReturnApply::getReturnName, queryParam.getReceiverKeyword())
                                    .or()
                                    .like(OmsOrderReturnApply::getReturnPhone, queryParam.getReceiverKeyword());
                        })
                )
                .convert(e -> {
                    OmsOrderReturnApplyResponseDTO dto = new OmsOrderReturnApplyResponseDTO();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                });
    }

    @Override
    public boolean delete(List<Long> ids) {
        return super.remove(Wrappers.<OmsOrderReturnApply>lambdaQuery()
                .in(OmsOrderReturnApply::getId, ids)
                .eq(OmsOrderReturnApply::getStatus,3)
        );
    }

    @Override
    public OmsOrderReturnApplyResultResponseDTO getItem(Long id) {
        return baseMapper.getDetail(id);
    }

    @Override
    public boolean updateStatus(Long id, OmsUpdateStatusParamRequestDTO statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        if(status.equals(1)){
            //确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(2)){
            //完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        }else if(status.equals(3)){
            //拒绝退货
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else{
            return false;
        }
        return super.updateById(returnApply);
    }
}
