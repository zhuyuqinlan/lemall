package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeRecommendProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeRecommendProductResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeRecommendProduct;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeRecommendProductMapper;

import java.util.List;

/**
 * 首页推荐商品管理 Service
 */
@Service
public class SmsHomeRecommendProductService extends ServiceImpl<SmsHomeRecommendProductMapper, SmsHomeRecommendProduct> {

    /**
     * 添加首页推荐
     * @param homeBrandList 请求参数列表
     * @return 成功标志
     */
    public boolean create(List<SmsHomeRecommendProductRequestDTO> homeBrandList) {
        List<SmsHomeRecommendProduct> smsHomeRecommendProducts = homeBrandList.stream().map(e -> {
            SmsHomeRecommendProduct smsHomeRecommendProduct = new SmsHomeRecommendProduct();
            BeanUtils.copyProperties(e, smsHomeRecommendProduct);
            smsHomeRecommendProduct.setRecommendStatus(1);
            smsHomeRecommendProduct.setSort(0);
            return smsHomeRecommendProduct;
        }).toList();
        return saveBatch(smsHomeRecommendProducts);
    }

    /**
     * 修改推荐排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    public boolean updateSort(Long id, Integer sort) {
        return update(Wrappers.<SmsHomeRecommendProduct>lambdaUpdate()
                .eq(SmsHomeRecommendProduct::getId, id)
                .set(SmsHomeRecommendProduct::getSort, sort)
        );
    }

    /**
     * 批量删除推荐
     * @param ids ids
     * @return 成功标志
     */
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 批量修改推荐状态
     * @param ids ids
     * @param recommendStatus 推荐状态
     * @return 成功标志
     */
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return update(Wrappers.<SmsHomeRecommendProduct>lambdaUpdate()
                .in(SmsHomeRecommendProduct::getId, ids)
                .set(SmsHomeRecommendProduct::getRecommendStatus, recommendStatus)
        );
    }

    /**
     * 分页查询推荐
     * @param productName 商品名
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsHomeRecommendProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeRecommendProduct>lambdaQuery()
                .like(StringUtils.hasText(productName), SmsHomeRecommendProduct::getProductName, productName)
                .eq(recommendStatus != null, SmsHomeRecommendProduct::getRecommendStatus, recommendStatus)
        ).convert(e -> {
            SmsHomeRecommendProductResponseDTO dto = new SmsHomeRecommendProductResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
