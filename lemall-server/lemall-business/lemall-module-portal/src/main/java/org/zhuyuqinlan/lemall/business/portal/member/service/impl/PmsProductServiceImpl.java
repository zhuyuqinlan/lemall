package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;
import org.zhuyuqinlan.lemall.business.portal.member.service.PmsProductService;
import org.zhuyuqinlan.lemall.common.mapper.PmsProductMapper;

@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService{

}
