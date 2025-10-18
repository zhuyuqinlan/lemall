package org.zhuyuqinlan.lemall.business.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.order.dao.OmsOrderReturnApplyDao;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsReturnApplyQueryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.request.OmsUpdateStatusParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.order.dto.response.OmsOrderReturnApplyResultResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderReturnApply;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderReturnApplyMapper;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OmsOrderReturnApplyService extends ServiceImpl<OmsOrderReturnApplyMapper, OmsOrderReturnApply> {

    private final OmsOrderReturnApplyDao omsOrderReturnApplyDao;

    /**
     * 分页查询退货申请
     */
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

    /**
     * 批量删除申请
     */
    public boolean delete(List<Long> ids) {
        return super.remove(Wrappers.<OmsOrderReturnApply>lambdaQuery()
                .in(OmsOrderReturnApply::getId, ids)
                .eq(OmsOrderReturnApply::getStatus, 3)
        );
    }

    /**
     * 获取退货申请详情
     */
    public OmsOrderReturnApplyResultResponseDTO getItem(Long id) {
        return omsOrderReturnApplyDao.getDetail(id);
    }

    /**
     * 修改申请状态
     */
    public boolean updateStatus(Long id, OmsUpdateStatusParamRequestDTO statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        returnApply.setId(id);

        if (status.equals(1)) { // 确认退货
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else if (status.equals(2)) { // 完成退货
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        } else if (status.equals(3)) { // 拒绝退货
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else {
            return false;
        }
        return super.updateById(returnApply);
    }
}
