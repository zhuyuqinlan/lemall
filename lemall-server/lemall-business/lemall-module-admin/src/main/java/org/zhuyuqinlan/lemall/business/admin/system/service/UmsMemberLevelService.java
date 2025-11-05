package org.zhuyuqinlan.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.business.admin.product.dto.UmsMemberLevelDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberLevel;
import org.zhuyuqinlan.lemall.common.mapper.UmsMemberLevelMapper;

import java.util.List;

@Service
public class UmsMemberLevelService extends ServiceImpl<UmsMemberLevelMapper, UmsMemberLevel> {
    /**
     * 查询所有会员等级
     */
    public List<UmsMemberLevelDTO> listLevel(Integer defaultStatus) {
        return super.list(Wrappers.<UmsMemberLevel>lambdaQuery()
                .eq(UmsMemberLevel::getDefaultStatus, defaultStatus)
        ).stream().map(e -> {
            UmsMemberLevelDTO dto = new UmsMemberLevelDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }
}
