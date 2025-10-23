package org.zhuyuqinlan.lemall.business.portal.home.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;

import java.math.BigDecimal;

@Getter
@Setter
public class FlashPromotionProduct extends PmsProductDTO {
    private BigDecimal flashPromotionPrice;
    private Integer flashPromotionCount;
    private Integer flashPromotionLimit;
}
