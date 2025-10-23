package org.zhuyuqinlan.lemall.business.portal.member.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.UmsMemberReceiveAddressRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.UmsMemberReceiveAddressDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberReceiveAddress;
import org.zhuyuqinlan.lemall.common.mapper.UmsMemberReceiveAddressMapper;

import java.util.List;

@Service
public class UmsMemberReceiveAddressService extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress> {

    /**
     * 添加收货地址
     */
    public boolean add(UmsMemberReceiveAddressRequestDTO address) {
        UmsMemberReceiveAddress entity = new UmsMemberReceiveAddress();
        BeanUtils.copyProperties(address, entity);
        return super.save(entity);
    }

    /**
     * 删除收货地址
     */
    public boolean delete(Long id) {
        Long memberId = Long.parseLong(StpMemberUtil.getLoginId().toString());
        return super.remove(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                .eq(UmsMemberReceiveAddress::getId, id)
                .eq(UmsMemberReceiveAddress::getMemberId, memberId)
        );
    }

    /**
     * 修改收货地址
     */
    @Transactional
    public boolean updateAddress(Long id, UmsMemberReceiveAddressRequestDTO address) {
        address.setId(null);
        Long memberId = Long.parseLong(StpMemberUtil.getLoginId().toString());

        // 如果设置为默认地址，先取消该用户的其他默认地址
        if (address.getDefaultStatus() == 1) {
            super.update(Wrappers.<UmsMemberReceiveAddress>lambdaUpdate()
                    .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                    .eq(UmsMemberReceiveAddress::getDefaultStatus, 1)
                    .ne(UmsMemberReceiveAddress::getId, id)
                    .set(UmsMemberReceiveAddress::getDefaultStatus, 0)
            );
        }

        UmsMemberReceiveAddress entity = new UmsMemberReceiveAddress();
        BeanUtils.copyProperties(address, entity);

        return super.update(entity, Wrappers.<UmsMemberReceiveAddress>lambdaUpdate()
                .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                .eq(UmsMemberReceiveAddress::getId, id)
        );
    }

    /**
     * 返回当前用户的收货地址列表
     */
    public List<UmsMemberReceiveAddressDTO> listAddress() {
        Long memberId = Long.parseLong(StpMemberUtil.getLoginId().toString());
        return super.list(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                        .eq(UmsMemberReceiveAddress::getMemberId, memberId))
                .stream().map(e -> {
                    UmsMemberReceiveAddressDTO dto = new UmsMemberReceiveAddressDTO();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                }).toList();
    }

    /**
     * 获取单个地址详情
     */
    public UmsMemberReceiveAddressDTO getItem(Long id) {
        Long memberId = Long.parseLong(StpMemberUtil.getLoginId().toString());
        UmsMemberReceiveAddress entity = super.getOne(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                        .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                        .eq(UmsMemberReceiveAddress::getId, id),
                false
        );
        UmsMemberReceiveAddressDTO dto = new UmsMemberReceiveAddressDTO();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
