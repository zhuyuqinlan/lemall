package org.zhuyuqinlan.lemall.business.admin.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.common.entity.CmsSubject;
import org.zhuyuqinlan.lemall.common.mapper.CmsSubjectMapper;
import org.zhuyuqinlan.lemall.business.admin.content.dto.response.CmsSubjectResponseDTO;

import java.util.List;

@Service
public class CmsSubjectService extends ServiceImpl<CmsSubjectMapper, CmsSubject> {

    /**
     * 获取全部商品专题
     * @return 结果
     */
    public List<CmsSubjectResponseDTO> listAll() {
        return super.list().stream().map(e -> {
            CmsSubjectResponseDTO dto = new CmsSubjectResponseDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        }).toList();
    }

    /**
     * 根据专题名称分页获取专题
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 结果
     */
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
