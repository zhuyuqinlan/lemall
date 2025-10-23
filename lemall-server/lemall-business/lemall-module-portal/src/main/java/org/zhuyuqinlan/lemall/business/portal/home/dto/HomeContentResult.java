package org.zhuyuqinlan.lemall.business.portal.home.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.member.dto.PmsProductDTO;

import java.util.List;

@Getter
@Setter
public class HomeContentResult {
    //轮播广告
    private List<SmsHomeAdvertiseDTO> advertiseList;
    //推荐品牌
    private List<PmsBrandDTO> brandList;
    //当前秒杀场次
    private HomeFlashPromotion homeFlashPromotion;
    //新品推荐
    private List<PmsProductDTO> newProductList;
    //人气推荐
    private List<PmsProductDTO> hotProductList;
    //推荐专题
    private List<CmsSubjectDTO> subjectList;
}
