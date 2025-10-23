package org.zhuyuqinlan.lemall.business.portal.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.domain.MemberReadHistory;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.MemberReadHistoryRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.MemberReadHistoryDTO;
import org.zhuyuqinlan.lemall.business.portal.member.repository.MemberReadHistoryRepository;
import org.zhuyuqinlan.lemall.business.portal.sso.dto.UmsMemberDTO;
import org.zhuyuqinlan.lemall.business.portal.sso.service.UmsMemberService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberReadHistoryService {
    private final MemberReadHistoryRepository memberReadHistoryRepository;
    private final UmsMemberService umsMemberService;

    /**
     * 生成浏览记录
     */
    public int create(MemberReadHistoryRequestDTO memberReadHistory) {
        UmsMemberDTO currentMember = umsMemberService.getCurrentMember();
        memberReadHistory.setMemberId(currentMember.getId());
        memberReadHistory.setMemberNickname(currentMember.getNickname());
        memberReadHistory.setMemberIcon(currentMember.getIcon());
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        MemberReadHistory memberReadHistory1 = new MemberReadHistory();
        BeanUtils.copyProperties(memberReadHistory, memberReadHistory1);
        memberReadHistoryRepository.save(memberReadHistory1);
        return 1;
    }

    /**
     * 批量删除浏览记录
     */
    public int delete(List<String> ids) {
        List<MemberReadHistory> deleteList = new ArrayList<>();
        for(String id:ids){
            MemberReadHistory memberReadHistory = new MemberReadHistory();
            memberReadHistory.setId(id);
            deleteList.add(memberReadHistory);
        }
        memberReadHistoryRepository.deleteAll(deleteList);
        return ids.size();
    }

    /**
     * 分页获取用户浏览历史记录
     */
    public Page<MemberReadHistoryDTO> list(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<MemberReadHistory> page = memberReadHistoryRepository
                .findByMemberIdOrderByCreateTimeDesc(
                        Long.parseLong(StpMemberUtil.getLoginId().toString()),
                        pageable
                );

        // 转换实体到 DTO
        return page.map(history -> {
            MemberReadHistoryDTO dto = new MemberReadHistoryDTO();
            BeanUtils.copyProperties(history, dto);
            return dto;
        });
    }


    /**
     * 清空浏览记录
     */
    public void clear() {
        memberReadHistoryRepository.deleteAllByMemberId(Long.parseLong(StpMemberUtil.getLoginId().toString()));
    }
}
