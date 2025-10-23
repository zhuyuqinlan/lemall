package org.zhuyuqinlan.lemall.business.portal.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.zhuyuqinlan.lemall.business.portal.member.domain.MemberReadHistory;

public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory,String> {
    Page<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);
    void deleteAllByMemberId(Long memberId);
}
