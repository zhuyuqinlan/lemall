package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.SmsCouponMapper;
import org.zhuyuqinlan.lemall.business.portal.member.service.SmsCouponService;
@Service("portalSmsCouponServiceImpl")
public class SmsCouponServiceImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements SmsCouponService {

}
