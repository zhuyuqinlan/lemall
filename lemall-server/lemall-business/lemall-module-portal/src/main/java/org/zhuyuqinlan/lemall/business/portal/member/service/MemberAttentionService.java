package org.zhuyuqinlan.lemall.business.portal.member.service;


import org.springframework.data.domain.Page;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberBrandAttentionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.MemberBrandAttentionResponseDTO;

public interface MemberAttentionService {

    /**
     * 添加关注
     */
    int add(MemberBrandAttentionRequestDTO memberBrandAttention);

    /**
     * 取消关注
     */
    int delete(Long brandId);

    /**
     * 获取用户关注列表
     */
    Page<MemberBrandAttentionResponseDTO> list(Integer pageNum, Integer pageSize);

    /**
     * 获取用户关注详情
     */
    MemberBrandAttentionResponseDTO detail(Long brandId);

    /**
     * 清空关注列表
     */
    void clear();
}
