package org.nanguo.lemall.business.admin.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.order.mapper.OmsOrderItemMapper;
import org.nanguo.lemall.common.entity.OmsOrderItem;
import org.nanguo.lemall.business.admin.order.service.OmsOrderItemService;
@Service
public class OmsOrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements OmsOrderItemService{

}
