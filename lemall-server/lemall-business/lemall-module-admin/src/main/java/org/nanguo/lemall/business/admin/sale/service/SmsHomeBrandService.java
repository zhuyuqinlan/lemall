package org.nanguo.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsHomeBrandRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsHomeBrandResponseDTO;
import org.nanguo.lemall.common.entity.SmsHomeBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SmsHomeBrandService extends IService<SmsHomeBrand>{


    /**
     * 添加首页推荐品牌
     * @param homeBrandList 请求参数列表
     * @return 成功标志
     */
    boolean create(List<SmsHomeBrandRequestDTO> homeBrandList);

    /**
     * 修改品牌排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐品牌
     * @param ids ids
     * @return 成功标志
     */
    boolean delete(List<Long> ids);

    /**
     * 批量修改推荐状态
     * @param ids ids
     * @param recommendStatus 请求参数
     * @return 成功标志
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐品牌
     * @param brandName 品牌名称
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<SmsHomeBrandResponseDTO> listPage(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
