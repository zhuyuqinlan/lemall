package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeBrandRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeBrandResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeBrand;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeBrandMapper;

import java.util.List;

/**
 * 首页推荐品牌管理 Service
 */
@Service
public class SmsHomeBrandService extends ServiceImpl<SmsHomeBrandMapper, SmsHomeBrand> {

    /**
     * 添加首页推荐品牌
     * @param homeBrandList 请求参数列表
     * @return 成功标志
     */
    public boolean create(List<SmsHomeBrandRequestDTO> homeBrandList) {
        List<SmsHomeBrand> smsHomeBrands = homeBrandList.stream().map(e -> {
            SmsHomeBrand smsHomeBrand = new SmsHomeBrand();
            BeanUtils.copyProperties(e, smsHomeBrand);
            return smsHomeBrand;
        }).toList();
        return saveBatch(smsHomeBrands);
    }

    /**
     * 修改品牌排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    public boolean updateSort(Long id, Integer sort) {
        return update(Wrappers.<SmsHomeBrand>lambdaUpdate()
                .eq(SmsHomeBrand::getId, id)
                .set(SmsHomeBrand::getSort, sort)
        );
    }

    /**
     * 批量删除推荐品牌
     * @param ids ids
     * @return 成功标志
     */
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 批量修改推荐状态
     * @param ids ids
     * @param recommendStatus 请求参数
     * @return 成功标志
     */
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return update(Wrappers.<SmsHomeBrand>lambdaUpdate()
                .in(SmsHomeBrand::getId, ids)
                .set(SmsHomeBrand::getRecommendStatus, recommendStatus)
        );
    }

    /**
     * 分页查询推荐品牌
     * @param brandName 品牌名称
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsHomeBrandResponseDTO> listPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeBrand>lambdaQuery()
                .like(StringUtils.hasText(brandName), SmsHomeBrand::getBrandName, brandName)
                .eq(recommendStatus != null, SmsHomeBrand::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeBrand::getSort)
        ).convert(e -> {
            SmsHomeBrandResponseDTO dto = new SmsHomeBrandResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
