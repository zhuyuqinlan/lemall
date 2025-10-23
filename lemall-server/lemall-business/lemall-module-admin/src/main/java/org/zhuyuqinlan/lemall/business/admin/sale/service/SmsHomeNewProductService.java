package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeNewProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsHomeNewProductDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeNewProduct;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeNewProductMapper;

import java.util.List;

/**
 * 首页新品推荐管理 Service
 */
@Service
public class SmsHomeNewProductService extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> {

    /**
     * 添加首页新品推荐
     * @param homeBrandList 请求参数列表
     * @return 成功标志
     */
    public boolean create(List<SmsHomeNewProductRequestDTO> homeBrandList) {
        List<SmsHomeNewProduct> smsHomeNewProducts = homeBrandList.stream().map(e -> {
            SmsHomeNewProduct smsHomeNewProduct = new SmsHomeNewProduct();
            BeanUtils.copyProperties(e, smsHomeNewProduct);
            smsHomeNewProduct.setRecommendStatus(1);
            smsHomeNewProduct.setSort(0);
            return smsHomeNewProduct;
        }).toList();
        return saveBatch(smsHomeNewProducts);
    }

    /**
     * 修改推荐排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    public boolean updateSort(Long id, Integer sort) {
        return update(Wrappers.<SmsHomeNewProduct>lambdaUpdate()
                .eq(SmsHomeNewProduct::getId, id)
                .set(SmsHomeNewProduct::getSort, sort)
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
        return update(Wrappers.<SmsHomeNewProduct>lambdaUpdate()
                .in(SmsHomeNewProduct::getId, ids)
                .set(SmsHomeNewProduct::getRecommendStatus, recommendStatus)
        );
    }

    /**
     * 分页查询推荐
     * @param productName 产品名称
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsHomeNewProductDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeNewProduct>lambdaQuery()
                .like(StringUtils.hasText(productName), SmsHomeNewProduct::getProductName, productName)
                .eq(recommendStatus != null, SmsHomeNewProduct::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeNewProduct::getSort)
        ).convert(e -> {
            SmsHomeNewProductDTO responseDTO = new SmsHomeNewProductDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        });
    }
}
