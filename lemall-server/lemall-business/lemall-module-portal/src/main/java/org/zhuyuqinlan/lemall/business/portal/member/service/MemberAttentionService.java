package org.zhuyuqinlan.lemall.business.portal.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.domain.MemberBrandAttention;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberBrandAttentionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.MemberBrandAttentionDTO;
import org.zhuyuqinlan.lemall.business.portal.member.repository.MemberBrandAttentionRepository;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.UmsMemberDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class MemberAttentionService {

    private final MemberBrandAttentionRepository memberBrandAttentionRepository;
    private final UmsMemberService memberService;

    // ======================= 添加关注 =======================
    public int add(MemberBrandAttentionRequestDTO memberBrandAttention) {
        int count = 0;
        UmsMemberDTO currentMember = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(currentMember.getId());
        memberBrandAttention.setMemberNickname(currentMember.getNickname());
        memberBrandAttention.setMemberIcon(currentMember.getIcon());
        memberBrandAttention.setCreateTime(new Date());

        MemberBrandAttention findAttention = memberBrandAttentionRepository.findByMemberIdAndBrandId(
                memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId()
        );
        if (findAttention == null) {
            MemberBrandAttention entity = new MemberBrandAttention();
            BeanUtils.copyProperties(memberBrandAttention, entity);
            memberBrandAttentionRepository.save(entity);
            count = 1;
        }
        return count;
    }

    // ======================= 取消关注 =======================
    public int delete(Long brandId) {
        return memberBrandAttentionRepository.deleteByMemberIdAndBrandId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), brandId
        );
    }

    // ======================= 获取关注列表 =======================
    public Page<MemberBrandAttentionDTO> list(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<MemberBrandAttention> page = memberBrandAttentionRepository.findByMemberId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), pageable
        );

        return page.map(entity -> {
            MemberBrandAttentionDTO dto = new MemberBrandAttentionDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    // ======================= 获取关注详情 =======================
    public MemberBrandAttentionDTO detail(Long brandId) {
        MemberBrandAttention entity = memberBrandAttentionRepository.findByMemberIdAndBrandId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), brandId
        );
        MemberBrandAttentionDTO dto = new MemberBrandAttentionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    // ======================= 清空关注 =======================
    public void clear() {
        memberBrandAttentionRepository.deleteAllByMemberId(Long.parseLong(StpMemberUtil.getLoginId().toString()));
    }
}
