package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsHomeRecommendSubjectRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.response.SmsHomeRecommendSubjectResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.SmsHomeRecommendSubject;
import org.zhuyuqinlan.lemall.common.mapper.SmsHomeRecommendSubjectMapper;

import java.util.List;

/**
 * 首页推荐专题管理 Service
 */
@Service
public class SmsHomeRecommendSubjectService extends ServiceImpl<SmsHomeRecommendSubjectMapper, SmsHomeRecommendSubject> {

    /**
     * 添加首页推荐专题
     * @param homeBrandList 参数列表
     * @return 成功标志
     */
    public boolean create(List<SmsHomeRecommendSubjectRequestDTO> homeBrandList) {
        List<SmsHomeRecommendSubject> smsHomeRecommendSubjects = homeBrandList.stream().map(e -> {
            SmsHomeRecommendSubject smsHomeRecommendSubject = new SmsHomeRecommendSubject();
            BeanUtils.copyProperties(e, smsHomeRecommendSubject);
            smsHomeRecommendSubject.setRecommendStatus(1);
            smsHomeRecommendSubject.setSort(0);
            return smsHomeRecommendSubject;
        }).toList();
        return saveBatch(smsHomeRecommendSubjects);
    }

    /**
     * 修改推荐排序
     * @param id id
     * @param sort 排序
     * @return 成功标志
     */
    public boolean updateSort(Long id, Integer sort) {
        return update(Wrappers.<SmsHomeRecommendSubject>lambdaUpdate()
                .eq(SmsHomeRecommendSubject::getId, id)
                .set(SmsHomeRecommendSubject::getSort, sort)
        );
    }

    /**
     * 批量删除推荐
     * @param ids ids
     * @return 成功标志
     */
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 批量修改推荐状态
     * @param ids ids
     * @param recommendStatus 推荐状态
     * @return 成功标志
     */
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return update(Wrappers.<SmsHomeRecommendSubject>lambdaUpdate()
                .in(SmsHomeRecommendSubject::getId, ids)
                .set(SmsHomeRecommendSubject::getRecommendStatus, recommendStatus)
        );
    }

    /**
     * 分页查询推荐
     * @param subjectName 专题名称
     * @param recommendStatus 推荐状态
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsHomeRecommendSubjectResponseDTO> listPage(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<SmsHomeRecommendSubject>lambdaQuery()
                .like(StringUtils.hasText(subjectName), SmsHomeRecommendSubject::getSubjectName, subjectName)
                .eq(recommendStatus != null, SmsHomeRecommendSubject::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeRecommendSubject::getSort)
        ).convert(e -> {
            SmsHomeRecommendSubjectResponseDTO responseDTO = new SmsHomeRecommendSubjectResponseDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        });
    }
}
