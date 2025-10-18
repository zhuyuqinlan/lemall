package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.entity.SmsFlashPromotionProductRelation;
import org.zhuyuqinlan.lemall.common.mapper.SmsFlashPromotionProductRelationMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.dao.SmsFlashPromotionProductRelationDao;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionProductRelationRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 秒杀活动和商品关联管理Service
 */
@Service
@RequiredArgsConstructor
public class SmsFlashPromotionProductRelationService extends ServiceImpl<SmsFlashPromotionProductRelationMapper, SmsFlashPromotionProductRelation> {

    private final SmsFlashPromotionProductRelationDao smsFlashPromotionProductRelationDao;

    /**
     * 批量选择商品添加关联
     * @param relationList 请求参数列表
     * @return 成功标志
     */
    public boolean create(List<SmsFlashPromotionProductRelationRequestDTO> relationList) {
        List<SmsFlashPromotionProductRelation> poList = relationList.stream().map(dto -> {
            SmsFlashPromotionProductRelation po = new SmsFlashPromotionProductRelation();
            BeanUtils.copyProperties(dto, po);
            return po;
        }).collect(Collectors.toList());
        return saveBatch(poList);
    }

    /**
     * 修改关联相关信息
     * @param id id
     * @param relation 请求参数
     * @return 成功标志
     */
    public boolean updatePro(Long id, SmsFlashPromotionProductRelationRequestDTO relation) {
        SmsFlashPromotionProductRelation po = new SmsFlashPromotionProductRelation();
        BeanUtils.copyProperties(relation, po);
        po.setId(id);
        return updateById(po);
    }

    /**
     * 删除关联
     * @param id id
     * @return 成功标志
     */
    public boolean delete(Long id) {
        return removeById(id);
    }

    /**
     * 获取管理商品促销信息
     * @param id id
     * @return 返回结果
     */
    public SmsFlashPromotionProductRelationResponseDTO getItem(Long id) {
        SmsFlashPromotionProductRelation relation = getById(id);
        if (relation == null) return null;
        SmsFlashPromotionProductRelationResponseDTO dto = new SmsFlashPromotionProductRelationResponseDTO();
        BeanUtils.copyProperties(relation, dto);
        return dto;
    }

    /**
     * 分页查询不同场次关联及商品信息
     * @param flashPromotionId flashPromotionId
     * @param flashPromotionSessionId flashPromotionSessionId
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsFlashPromotionProductResponseDTO> listPage(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        return smsFlashPromotionProductRelationDao.getList(new Page<>(pageNum, pageSize), flashPromotionId, flashPromotionSessionId);
    }

    /**
     * 根据活动和场次id获取商品关系数量
     * @param flashPromotionId flashPromotionId
     * @param id id
     * @return 结果
     */
    public long getCount(Long flashPromotionId, Long id) {
        return count(Wrappers.<SmsFlashPromotionProductRelation>lambdaQuery()
                .eq(SmsFlashPromotionProductRelation::getFlashPromotionId, flashPromotionId)
                .eq(SmsFlashPromotionProductRelation::getId, id)
        );
    }
}
