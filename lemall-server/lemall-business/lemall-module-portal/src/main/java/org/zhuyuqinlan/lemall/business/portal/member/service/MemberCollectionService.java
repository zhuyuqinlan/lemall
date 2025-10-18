package org.zhuyuqinlan.lemall.business.portal.member.service;

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
import org.zhuyuqinlan.lemall.business.portal.sso.dto.response.UmsMemberResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;


@Service
@RequiredArgsConstructor
public class MemberCollectionService {

    private final MemberProductCollectionRepository productCollectionRepository;
    private final UmsMemberService umsMemberService;

    // ======================= 添加收藏 =======================
    public int add(MemberProductCollectionRequestDTO productCollection) {
        int count = 0;
        UmsMemberResponseDTO currentMember = umsMemberService.getCurrentMember();
        productCollection.setMemberId(currentMember.getId());
        productCollection.setMemberNickname(currentMember.getNickname());
        productCollection.setMemberIcon(currentMember.getIcon());

        MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(
                productCollection.getMemberId(), productCollection.getProductId()
        );
        if (findCollection == null) {
            MemberProductCollection entity = new MemberProductCollection();
            BeanUtils.copyProperties(productCollection, entity);
            productCollectionRepository.save(entity);
            count = 1;
        }
        return count;
    }

    // ======================= 删除收藏 =======================
    public int delete(Long productId) {
        return productCollectionRepository.deleteByMemberIdAndProductId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), productId
        );
    }

    // ======================= 收藏列表 =======================
    public Page<MemberProductCollectionResponseDTO> list(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<MemberProductCollection> page = productCollectionRepository.findByMemberId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), pageable
        );

        return page.map(entity -> {
            MemberProductCollectionResponseDTO dto = new MemberProductCollectionResponseDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    // ======================= 收藏详情 =======================
    public MemberProductCollectionResponseDTO detail(Long productId) {
        MemberProductCollection entity = productCollectionRepository.findByMemberIdAndProductId(
                Long.parseLong(StpMemberUtil.getLoginId().toString()), productId
        );
        MemberProductCollectionResponseDTO dto = new MemberProductCollectionResponseDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    // ======================= 清空收藏 =======================
    public void clear() {
        productCollectionRepository.deleteAllByMemberId(Long.parseLong(StpMemberUtil.getLoginId().toString()));
    }
}
