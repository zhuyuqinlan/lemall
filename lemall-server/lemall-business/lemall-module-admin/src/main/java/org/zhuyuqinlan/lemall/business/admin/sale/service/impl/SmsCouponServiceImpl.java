package org.zhuyuqinlan.lemall.business.admin.sale.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponProductCategoryRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponProductRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponParamResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsCouponResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponProductCategoryRelationService;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponProductRelationService;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponProductCategoryRelation;
import org.zhuyuqinlan.lemall.common.entity.SmsCouponProductRelation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.sale.dao.SmsCouponDao;
import org.zhuyuqinlan.lemall.common.entity.SmsCoupon;
import org.zhuyuqinlan.lemall.business.admin.sale.service.SmsCouponService;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.common.mapper.SmsCouponMapper;

@Service
@RequiredArgsConstructor
public class SmsCouponServiceImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements SmsCouponService {
    private final SmsCouponProductRelationService smsCouponProductRelationService;
    private final SmsCouponProductCategoryRelationService smsCouponProductCategoryRelationService;
    private final SmsCouponDao smsCouponDao;

    @Override
    public boolean create(SmsCouponParamRequestDTO couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        SmsCoupon smsCoupon = new SmsCoupon();
        BeanUtils.copyProperties(couponParam, smsCoupon);
        super.save(smsCoupon);
        //插入优惠券和商品关系表
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelationRequestDTO productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            SmsCouponProductRelation smsCouponProductRelation = new SmsCouponProductRelation();
            BeanUtils.copyProperties(couponParam.getProductRelationList(), smsCouponProductRelation);
            smsCouponProductRelationService.save(smsCouponProductRelation);
        }
        //插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelationRequestDTO couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            SmsCouponProductCategoryRelation smsCouponProductCategoryRelation = new SmsCouponProductCategoryRelation();
            BeanUtils.copyProperties(couponParam.getProductCategoryRelationList(), smsCouponProductCategoryRelation);
            smsCouponProductCategoryRelationService.save(smsCouponProductCategoryRelation);
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        //删除优惠券
        boolean b = super.removeById(id);
        //删除商品关联
        smsCouponProductRelationService.remove(Wrappers.<SmsCouponProductRelation>lambdaQuery()
                .eq(SmsCouponProductRelation::getCouponId, id));
        //删除商品分类关联
        smsCouponProductCategoryRelationService.remove(Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery()
                .eq(SmsCouponProductCategoryRelation::getCouponId, id));
        return b;
    }

    @Override
    public boolean updateCoupon(Long id, SmsCouponParamRequestDTO couponParam) {
        couponParam.setId(id);
        SmsCoupon smsCoupon = new SmsCoupon();
        BeanUtils.copyProperties(couponParam, smsCoupon);
        boolean b = super.updateById(smsCoupon);
        //删除后插入优惠券和商品关系表
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelationRequestDTO productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            //删除商品关联
            smsCouponProductRelationService.remove(Wrappers.<SmsCouponProductRelation>lambdaQuery()
                    .eq(SmsCouponProductRelation::getCouponId, id));
            SmsCouponProductRelation smsCouponProductRelation = new SmsCouponProductRelation();
            BeanUtils.copyProperties(couponParam.getProductRelationList(), smsCouponProductRelation);
            smsCouponProductRelationService.save(smsCouponProductRelation);
        }
        //删除后插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelationRequestDTO couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            //删除商品分类关联
            smsCouponProductCategoryRelationService.remove(Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery()
                    .eq(SmsCouponProductCategoryRelation::getCouponId, id));
            SmsCouponProductCategoryRelation smsCouponProductCategoryRelation = new SmsCouponProductCategoryRelation();
            BeanUtils.copyProperties(couponParam.getProductCategoryRelationList(), smsCouponProductCategoryRelation);
            smsCouponProductCategoryRelationService.save(smsCouponProductCategoryRelation);
        }
        return b;
    }

    @Override
    public IPage<SmsCouponResponseDTO> listPage(String name, Integer type, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<SmsCoupon>lambdaQuery()
                .like(StringUtils.hasText(name), SmsCoupon::getName, name)
                .eq(type != null, SmsCoupon::getType, type)
        ).convert(e -> {
            SmsCouponResponseDTO smsCouponResponseDTO = new SmsCouponResponseDTO();
            BeanUtils.copyProperties(e, smsCouponResponseDTO);
            return smsCouponResponseDTO;
        });
    }

    @Override
    public SmsCouponParamResponseDTO getItem(Long id) {
        return smsCouponDao.getItem(id);
    }
}
