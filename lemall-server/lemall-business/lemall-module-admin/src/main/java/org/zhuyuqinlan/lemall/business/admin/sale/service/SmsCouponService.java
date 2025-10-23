package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.business.admin.sale.dao.SmsCouponDao;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponProductCategoryRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsCouponProductRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsCouponParamDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsCouponDTO;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.common.mapper.SmsCouponMapper;

/**
 * 优惠券管理Service
 */
@Service
@RequiredArgsConstructor
public class SmsCouponService extends ServiceImpl<SmsCouponMapper, SmsCoupon> {

    private final SmsCouponProductRelationService smsCouponProductRelationService;
    private final SmsCouponProductCategoryRelationService smsCouponProductCategoryRelationService;
    private final SmsCouponDao smsCouponDao;

    @Transactional
    public boolean create(SmsCouponParamRequestDTO couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);

        // 插入优惠券表
        SmsCoupon smsCoupon = new SmsCoupon();
        BeanUtils.copyProperties(couponParam, smsCoupon);
        super.save(smsCoupon);

        // 插入优惠券和商品关系表
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelationRequestDTO productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            SmsCouponProductRelation smsCouponProductRelation = new SmsCouponProductRelation();
            BeanUtils.copyProperties(couponParam.getProductRelationList(), smsCouponProductRelation);
            smsCouponProductRelationService.save(smsCouponProductRelation);
        }

        // 插入优惠券和商品分类关系表
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

    @Transactional
    public boolean delete(Long id) {
        // 删除优惠券
        boolean b = super.removeById(id);

        // 删除商品关联
        smsCouponProductRelationService.remove(Wrappers.<SmsCouponProductRelation>lambdaQuery()
                .eq(SmsCouponProductRelation::getCouponId, id));

        // 删除商品分类关联
        smsCouponProductCategoryRelationService.remove(Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery()
                .eq(SmsCouponProductCategoryRelation::getCouponId, id));

        return b;
    }

    @Transactional
    public boolean updateCoupon(Long id, SmsCouponParamRequestDTO couponParam) {
        couponParam.setId(id);
        SmsCoupon smsCoupon = new SmsCoupon();
        BeanUtils.copyProperties(couponParam, smsCoupon);
        boolean b = super.updateById(smsCoupon);

        // 更新商品关联
        if (couponParam.getUseType().equals(2)) {
            smsCouponProductRelationService.remove(Wrappers.<SmsCouponProductRelation>lambdaQuery()
                    .eq(SmsCouponProductRelation::getCouponId, id));

            for (SmsCouponProductRelationRequestDTO productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            SmsCouponProductRelation smsCouponProductRelation = new SmsCouponProductRelation();
            BeanUtils.copyProperties(couponParam.getProductRelationList(), smsCouponProductRelation);
            smsCouponProductRelationService.save(smsCouponProductRelation);
        }

        // 更新商品分类关联
        if (couponParam.getUseType().equals(1)) {
            smsCouponProductCategoryRelationService.remove(Wrappers.<SmsCouponProductCategoryRelation>lambdaQuery()
                    .eq(SmsCouponProductCategoryRelation::getCouponId, id));

            for (SmsCouponProductCategoryRelationRequestDTO couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            SmsCouponProductCategoryRelation smsCouponProductCategoryRelation = new SmsCouponProductCategoryRelation();
            BeanUtils.copyProperties(couponParam.getProductCategoryRelationList(), smsCouponProductCategoryRelation);
            smsCouponProductCategoryRelationService.save(smsCouponProductCategoryRelation);
        }

        return b;
    }

    public IPage<SmsCouponDTO> listPage(String name, Integer type, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<SmsCoupon>lambdaQuery()
                .like(StringUtils.hasText(name), SmsCoupon::getName, name)
                .eq(type != null, SmsCoupon::getType, type)
        ).convert(e -> {
            SmsCouponDTO smsCouponResponseDTO = new SmsCouponDTO();
            BeanUtils.copyProperties(e, smsCouponResponseDTO);
            return smsCouponResponseDTO;
        });
    }

    public SmsCouponParamDTO getItem(Long id) {
        return smsCouponDao.getItem(id);
    }
}
