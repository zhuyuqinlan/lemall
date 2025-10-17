package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.domain.mogo.MemberBrandAttention;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberBrandAttentionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.MemberBrandAttentionResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.repository.MemberBrandAttentionRepository;
import org.zhuyuqinlan.lemall.business.portal.member.service.MemberAttentionService;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberAttentionServiceImpl implements MemberAttentionService {

    private final MemberBrandAttentionRepository memberBrandAttentionRepository;
    private final UmsMemberService memberService;

    @Override
    public int add(MemberBrandAttentionRequestDTO memberBrandAttention) {
        int count = 0;
        UmsMemberResponseDTO currentMember = memberService.getCurrentMember();
        memberBrandAttention.setMemberId(currentMember.getId());
        memberBrandAttention.setMemberNickname(currentMember.getNickname());
        memberBrandAttention.setMemberIcon(currentMember.getIcon());
        memberBrandAttention.setCreateTime(new Date());
        MemberBrandAttention findAttention = memberBrandAttentionRepository.findByMemberIdAndBrandId(memberBrandAttention.getMemberId(), memberBrandAttention.getBrandId());
        if (findAttention == null) {
            MemberBrandAttention memberBrandAttention1 = new MemberBrandAttention();
            BeanUtils.copyProperties(memberBrandAttention, memberBrandAttention1);
            memberBrandAttentionRepository.save(memberBrandAttention1);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long brandId) {
        return memberBrandAttentionRepository.deleteByMemberIdAndBrandId(Long.parseLong(StpMemberUtil.getLoginId().toString()),brandId);
    }

    @Override
    public Page<MemberBrandAttentionResponseDTO> list(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // 查询实体分页
        Page<MemberBrandAttention> page = memberBrandAttentionRepository.findByMemberId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), pageable
        );

        // 转成 DTO 分页
        return page.map(entity -> {
            MemberBrandAttentionResponseDTO dto = new MemberBrandAttentionResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    @Override
    public MemberBrandAttentionResponseDTO detail(Long brandId) {
        MemberBrandAttention byMemberIdAndBrandId = memberBrandAttentionRepository.findByMemberIdAndBrandId(Long.parseLong(StpMemberUtil.getLoginId().toString()), brandId);
        MemberBrandAttentionResponseDTO dto = new MemberBrandAttentionResponseDTO();
        BeanUtils.copyProperties(byMemberIdAndBrandId, dto);
        return dto;
    }

    @Override
    public void clear() {
        memberBrandAttentionRepository.deleteAllByMemberId(Long.parseLong(StpMemberUtil.getLoginId().toString()));
    }
}
