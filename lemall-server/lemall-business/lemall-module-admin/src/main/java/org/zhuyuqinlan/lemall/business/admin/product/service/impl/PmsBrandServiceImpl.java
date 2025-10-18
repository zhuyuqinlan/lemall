package org.zhuyuqinlan.lemall.business.admin.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsBrandRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsBrandResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.PmsProduct;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.mapper.PmsBrandMapper;
import org.zhuyuqinlan.lemall.common.entity.PmsBrand;
import org.zhuyuqinlan.lemall.business.admin.product.service.PmsBrandService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandMapper, PmsBrand> implements PmsBrandService {

    private final PmsProductService pmsProductService;

    @Override
    public IPage<PmsBrandResponseDTO> getList(String keyword, Integer pageNum, Integer pageSize) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<PmsBrand>lambdaQuery()
                .like(StringUtils.hasText(keyword), PmsBrand::getName, keyword)
                .orderByDesc(PmsBrand::getSort)
        ).convert(e -> {
            PmsBrandResponseDTO pmsBrandResponseDTO = new PmsBrandResponseDTO();
            BeanUtils.copyProperties(e, pmsBrandResponseDTO);
            return pmsBrandResponseDTO;
        });
    }

    @Override
    public List<PmsBrandResponseDTO> getListAll() {
        return super.list().stream().map(e -> {
            PmsBrandResponseDTO pmsBrandResponseDTO = new PmsBrandResponseDTO();
            BeanUtils.copyProperties(e, pmsBrandResponseDTO);
            return pmsBrandResponseDTO;
        }).toList();
    }

    @Override
    public boolean create(PmsBrandRequestDTO requestDTO) {
        PmsBrand pmsBrand = new PmsBrand();
        requestDTO.setId(null);
        BeanUtils.copyProperties(requestDTO, pmsBrand);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (!StringUtils.hasText(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return super.save(pmsBrand);
    }

    @Override
    public boolean updateBrand(Long id, PmsBrandRequestDTO requestDTO) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(requestDTO, pmsBrand);
        pmsBrand.setId(id);
        //如果创建时首字母为空，取名称的第一个为首字母
        if (!StringUtils.hasText(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        // 更新品牌时要更新商品中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        pmsProductService.update(product, Wrappers.<PmsProduct>lambdaUpdate().eq(PmsProduct::getBrandId, id));
        return super.updateById(pmsBrand);
    }

    @Override
    public boolean deleteBrandById(Long id) {
        // TODO 商品表关联了这个的，如果要删，要确认
        return super.removeById(id);
    }

    @Override
    public PmsBrandResponseDTO getBrandById(Long id) {
        PmsBrandResponseDTO pmsBrandResponseDTO = new PmsBrandResponseDTO();
        PmsBrand pmsBrand = super.getById(id);
        BeanUtils.copyProperties(pmsBrand, pmsBrandResponseDTO);
        return pmsBrandResponseDTO;
    }

    @Override
    public boolean deleteBrandBatch(List<Long> ids) {
        // TODO 商品表关联了这个的，如果要删，要确认
        return super.removeBatchByIds(ids);
    }

    @Override
    public boolean updateBrandByIds(List<Long> ids, Integer showStatus) {
        return super.update(Wrappers.<PmsBrand>lambdaUpdate()
                .in(PmsBrand::getId, ids)
                .set(PmsBrand::getShowStatus, showStatus)
        );
    }

    @Override
    public boolean updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        return super.update(Wrappers.<PmsBrand>lambdaUpdate()
                .in(PmsBrand::getId, ids)
                .set(PmsBrand::getFactoryStatus, factoryStatus)
        );
    }
}
