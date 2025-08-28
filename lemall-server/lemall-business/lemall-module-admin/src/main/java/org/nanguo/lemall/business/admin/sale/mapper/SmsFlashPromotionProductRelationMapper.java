package org.nanguo.lemall.business.admin.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductRelationResponseDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductResponseDTO;
import org.nanguo.lemall.common.entity.SmsFlashPromotionProductRelation;

public interface SmsFlashPromotionProductRelationMapper extends BaseMapper<SmsFlashPromotionProductRelation> {
    /**
     * 获取限时购及相关商品信息
     */
    IPage<SmsFlashPromotionProductResponseDTO> getList(Page<Object> objectPage, @Param("flashPromotionId") Long flashPromotionId, @Param("flashPromotionSessionId") Long flashPromotionSessionId);
}