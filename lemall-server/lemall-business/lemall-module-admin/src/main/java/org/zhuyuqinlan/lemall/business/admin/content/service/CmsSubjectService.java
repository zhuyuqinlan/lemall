package org.zhuyuqinlan.lemall.business.admin.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zhuyuqinlan.lemall.business.admin.content.dto.response.CmsSubjectResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.CmsSubject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CmsSubjectService extends IService<CmsSubject>{

    /**
     * 获取全部商品专题
     * @return 结果
     */
    List<CmsSubjectResponseDTO> listAll();

    /**
     * 根据专题名称分页获取专题
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 结果
     */
    IPage<CmsSubjectResponseDTO> listPage(String keyword, Integer pageNum, Integer pageSize);
}
