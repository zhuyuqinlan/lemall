package org.zhuyuqinlan.lemall.business.admin.content.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.content.dto.response.CmsPrefrenceAreaDTO;
import org.zhuyuqinlan.lemall.common.entity.CmsPrefrenceArea;
import org.zhuyuqinlan.lemall.common.mapper.CmsPrefrenceAreaMapper;

import java.util.List;

@Service
public class CmsPrefrenceAreaService extends ServiceImpl<CmsPrefrenceAreaMapper, CmsPrefrenceArea> {

    /**
     * 获取所有商品优选
     * @return 结果
     */
    public List<CmsPrefrenceAreaDTO> listAll() {
        return super.list().stream().map(e -> {
            CmsPrefrenceAreaDTO dto = new CmsPrefrenceAreaDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }
}
