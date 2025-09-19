package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeNewProductRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeNewProductResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeNewProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SmsHomeNewProductService extends IService<SmsHomeNewProduct>{


    /**
     * 添加首页推荐品牌
     * @param homeBrandList 请求参数
     * @return 成功标志
     */
    boolean create(List<SmsHomeNewProductRequestDTO> homeBrandList);

    /**
     * 修改推荐排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 批量修改推荐状态
     * @param ids ids
     * @param recommendStatus 推荐状态
     * @return 成功标志
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐
     * @param productName 产品名称
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<SmsHomeNewProductResponseDTO> listPage(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
