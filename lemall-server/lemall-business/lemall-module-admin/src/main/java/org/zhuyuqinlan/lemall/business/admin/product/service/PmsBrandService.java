package org.zhuyuqinlan.lemall.business.admin.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsBrandRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsBrandResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.PmsBrand;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;
import org.zhuyuqinlan.lemall.common.mapper.PmsBrandMapper;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductService;

import java.util.List;

/**
 * 品牌管理 Service
 */
@Service
@RequiredArgsConstructor
public class PmsBrandService extends ServiceImpl<PmsBrandMapper, PmsBrand> {

    private final PmsProductService pmsProductService;

    /**
     * 分页查询品牌列表
     */
    public IPage<PmsBrandResponseDTO> getList(String keyword, Integer pageNum, Integer pageSize) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<PmsBrand>lambdaQuery()
                .like(StringUtils.hasText(keyword), PmsBrand::getName, keyword)
                .orderByDesc(PmsBrand::getSort)
        ).convert(e -> {
            PmsBrandResponseDTO dto = new PmsBrandResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }

    /**
     * 获取所有品牌
     */
    public List<PmsBrandResponseDTO> getListAll() {
        return list().stream().map(e -> {
            PmsBrandResponseDTO dto = new PmsBrandResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }

    /**
     * 添加品牌
     */
    public boolean create(PmsBrandRequestDTO requestDTO) {
        PmsBrand pmsBrand = new PmsBrand();
        requestDTO.setId(null);
        BeanUtils.copyProperties(requestDTO, pmsBrand);
        if (!StringUtils.hasText(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return save(pmsBrand);
    }

    /**
     * 更新品牌
     */
    @Transactional
    public boolean updateBrand(Long id, PmsBrandRequestDTO requestDTO) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(requestDTO, pmsBrand);
        pmsBrand.setId(id);
        if (!StringUtils.hasText(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        // 更新品牌时要同步更新商品表中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        pmsProductService.update(product, Wrappers.<PmsProduct>lambdaUpdate()
                .eq(PmsProduct::getBrandId, id));
        return updateById(pmsBrand);
    }

    /**
     * 删除品牌
     */
    public boolean deleteBrandById(Long id) {
        // TODO 商品表关联了这个的，如果要删，要确认
        return removeById(id);
    }

    /**
     * 根据品牌id查询
     */
    public PmsBrandResponseDTO getBrandById(Long id) {
        PmsBrandResponseDTO dto = new PmsBrandResponseDTO();
        PmsBrand brand = getById(id);
        BeanUtils.copyProperties(brand, dto);
        return dto;
    }

    /**
     * 根据id批量删除品牌
     */
    public boolean deleteBrandBatch(List<Long> ids) {
        // TODO 商品表关联了这个的，如果要删，要确认
        return removeBatchByIds(ids);
    }

    /**
     * 批量更新显示状态
     */
    public boolean updateBrandByIds(List<Long> ids, Integer showStatus) {
        return update(Wrappers.<PmsBrand>lambdaUpdate()
                .in(PmsBrand::getId, ids)
                .set(PmsBrand::getShowStatus, showStatus)
        );
    }

    /**
     * 批量更新厂家制造商状态
     */
    public boolean updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        return update(Wrappers.<PmsBrand>lambdaUpdate()
                .in(PmsBrand::getId, ids)
                .set(PmsBrand::getFactoryStatus, factoryStatus)
        );
    }
}
