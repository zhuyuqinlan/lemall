package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.domain.mogo.MemberProductCollection;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberProductCollectionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.MemberProductCollectionResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.repository.MemberProductCollectionRepository;
import org.zhuyuqinlan.lemall.business.portal.member.service.MemberCollectionService;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;

@Service
@RequiredArgsConstructor
public class MemberCollectionServiceImpl implements MemberCollectionService {
    private final MemberProductCollectionRepository productCollectionRepository;
    private final UmsMemberService umsMemberService;

    @Override
    public int add(MemberProductCollectionRequestDTO productCollection) {
        int count = 0;
        UmsMemberResponseDTO currentMember = umsMemberService.getCurrentMember();
        productCollection.setMemberId(currentMember.getId());
        productCollection.setMemberNickname(currentMember.getNickname());
        productCollection.setMemberIcon(currentMember.getIcon());
        MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(productCollection.getMemberId(), productCollection.getProductId());
        if (findCollection == null) {
            MemberProductCollection memberProductCollection = new MemberProductCollection();
            BeanUtils.copyProperties(productCollection, memberProductCollection);
            productCollectionRepository.save(memberProductCollection);
            count = 1;
        }
        return count;
    }

    @Override
    public int delete(Long productId) {
        return productCollectionRepository.deleteByMemberIdAndProductId(Long.parseLong(StpMemberUtil.getLoginId().toString()), productId);
    }

    @Override
    public Page<MemberProductCollectionResponseDTO> list(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // 查询实体分页
        Page<MemberProductCollection> page = productCollectionRepository.findByMemberId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), pageable
        );

        // 转成 DTO 分页
        return page.map(entity -> {
            MemberProductCollectionResponseDTO dto = new MemberProductCollectionResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    @Override
    public MemberProductCollectionResponseDTO detail(Long productId) {
        MemberProductCollection byMemberIdAndProductId = productCollectionRepository.findByMemberIdAndProductId(Long.parseLong(StpMemberUtil.getLoginId().toString()), productId);
        MemberProductCollectionResponseDTO memberProductCollectionResponseDTO = new MemberProductCollectionResponseDTO();
        BeanUtils.copyProperties(byMemberIdAndProductId, memberProductCollectionResponseDTO);
        return memberProductCollectionResponseDTO;
    }

    @Override
    public void clear() {
        productCollectionRepository.deleteAllByMemberId(Long.parseLong(StpMemberUtil.getLoginId().toString()));
    }
}
