package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponMapperPortal;
import org.zhuyuqinlan.lemall.business.portal.member.service.SmsCouponServicePortal;
@Service("portalSmsCouponServiceImpl")
public class SmsCouponServiceImplPortal extends ServiceImpl<SmsCouponMapperPortal, SmsCoupon> implements SmsCouponServicePortal {

}
