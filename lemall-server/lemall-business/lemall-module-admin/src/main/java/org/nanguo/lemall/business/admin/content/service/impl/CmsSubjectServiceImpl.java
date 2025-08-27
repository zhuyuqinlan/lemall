package org.nanguo.lemall.business.admin.content.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nanguo.lemall.business.admin.content.dto.response.CmsSubjectResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nanguo.lemall.business.admin.content.mapper.CmsSubjectMapper;
import org.nanguo.lemall.common.entity.CmsSubject;
import org.nanguo.lemall.business.admin.content.service.CmsSubjectService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CmsSubjectServiceImpl extends ServiceImpl<CmsSubjectMapper, CmsSubject> implements CmsSubjectService {

    @Override
    public List<CmsSubjectResponseDTO> listAll() {
        return super.list().stream().map(e -> {
            CmsSubjectResponseDTO dto = new CmsSubjectResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }

    @Override
    public IPage<CmsSubjectResponseDTO> listPage(String keyword, Integer pageNum, Integer pageSize) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<CmsSubject>lambdaQuery()
                .like(StringUtils.hasText(keyword), CmsSubject::getTitle, keyword)
        ).convert(e -> {
            CmsSubjectResponseDTO dto = new CmsSubjectResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
    }
}
