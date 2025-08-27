package org.nanguo.lemall.business.admin.order.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.order.mapper.OmsCartItemMapper;
import org.nanguo.lemall.common.entity.OmsCartItem;
import org.nanguo.lemall.business.admin.order.service.OmsCartItemService;
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements OmsCartItemService{

}
