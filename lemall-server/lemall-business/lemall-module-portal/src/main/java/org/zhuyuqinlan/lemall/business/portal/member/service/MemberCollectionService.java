package org.zhuyuqinlan.lemall.business.portal.member.service;

import org.springframework.data.domain.Page;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberProductCollectionRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.MemberProductCollectionResponseDTO;

public interface MemberCollectionService {
    int add(MemberProductCollectionRequestDTO productCollection);

    int delete(Long productId);

    Page<MemberProductCollectionResponseDTO> list(Integer pageNum, Integer pageSize);

    MemberProductCollectionResponseDTO detail(Long productId);

    void clear();
}
