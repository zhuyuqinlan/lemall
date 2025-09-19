package org.zhuyuqinlan.lemall.business.admin.content.service;

import org.zhuyuqinlan.lemall.business.admin.content.dto.response.CmsPrefrenceAreaResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.CmsPrefrenceArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CmsPrefrenceAreaService extends IService<CmsPrefrenceArea>{


    /**
     * 获取所有商品优选
     * @return 结果
     */
    List<CmsPrefrenceAreaResponseDTO> listAll();
}
