package org.zhuyuqinlan.lemall.business.admin.sale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsFlashPromotionProductResponseDTO;

public interface SmsFlashPromotionProductRelationDao {
    /**
     * 获取限时购及相关商品信息
     */
    IPage<SmsFlashPromotionProductResponseDTO> getList(Page<Object> objectPage, @Param("flashPromotionId") Long flashPromotionId, @Param("flashPromotionSessionId") Long flashPromotionSessionId);
}