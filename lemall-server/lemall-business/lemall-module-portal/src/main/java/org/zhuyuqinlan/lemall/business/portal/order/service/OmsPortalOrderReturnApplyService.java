package org.zhuyuqinlan.lemall.business.portal.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.portal.order.dto.request.OmsOrderReturnApplyParam;
import org.zhuyuqinlan.lemall.common.entity.OmsOrderReturnApply;
import org.zhuyuqinlan.lemall.common.mapper.OmsOrderReturnApplyMapper;

import java.util.Date;

@Service
public class OmsPortalOrderReturnApplyService extends ServiceImpl<OmsOrderReturnApplyMapper, OmsOrderReturnApply> {
    public boolean create(OmsOrderReturnApplyParam returnApply) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply,realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        return super.save(realApply);
    }
}
