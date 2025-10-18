package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.auth.util.StpMemberUtil;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.UmsMemberReceiveAddressRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.UmsMemberReceiveAddressResponseDTO;
import org.zhuyuqinlan.lemall.common.mapper.UmsMemberReceiveAddressMapper;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberReceiveAddressService;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberReceiveAddress;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress> implements UmsMemberReceiveAddressService {

    @Override
    public boolean add(UmsMemberReceiveAddressRequestDTO address) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        BeanUtils.copyProperties(address, umsMemberReceiveAddress);
        return super.save(umsMemberReceiveAddress);
    }

    @Override
    public boolean delete(Long id) {
        return super.remove(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                .eq(UmsMemberReceiveAddress::getId, id)
                .eq(UmsMemberReceiveAddress::getMemberId, Long.parseLong(StpMemberUtil.getLoginId().toString()))
        );
    }

    @Override
    public boolean updateAddress(Long id, UmsMemberReceiveAddressRequestDTO address) {
        address.setId(null);
        Long memberId = Long.parseLong(StpMemberUtil.getLoginId().toString());

        // 如果要设为默认，先把该用户的其他默认地址取消
        if (address.getDefaultStatus() == 1) {
            super.update(Wrappers.<UmsMemberReceiveAddress>lambdaUpdate()
                    .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                    .eq(UmsMemberReceiveAddress::getDefaultStatus, 1)
                    .ne(UmsMemberReceiveAddress::getId, id)
                    .set(UmsMemberReceiveAddress::getDefaultStatus, 0)
            );
        }

        // 执行更新当前地址
        UmsMemberReceiveAddress entity = new UmsMemberReceiveAddress();
        BeanUtils.copyProperties(address, entity);

        return super.update(entity, Wrappers.<UmsMemberReceiveAddress>lambdaUpdate()
                .eq(UmsMemberReceiveAddress::getMemberId, memberId)
                .eq(UmsMemberReceiveAddress::getId, id)
        );
    }

    @Override
    public List<UmsMemberReceiveAddressResponseDTO> listAddress() {
        return super.list(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                .eq(UmsMemberReceiveAddress::getMemberId, Long.parseLong(StpMemberUtil.getLoginId().toString()))
        ).stream().map(e -> {
            UmsMemberReceiveAddressResponseDTO address = new UmsMemberReceiveAddressResponseDTO();
            BeanUtils.copyProperties(e, address);
            return address;
        }).toList();
    }

    @Override
    public UmsMemberReceiveAddressResponseDTO getItem(Long id) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = super.getOne(Wrappers.<UmsMemberReceiveAddress>lambdaQuery()
                .eq(UmsMemberReceiveAddress::getMemberId,Long.parseLong(StpMemberUtil.getLoginId().toString()))
                .eq(UmsMemberReceiveAddress::getId,id)
        ,false);
        UmsMemberReceiveAddressResponseDTO addressResponseDTO = new UmsMemberReceiveAddressResponseDTO();
        BeanUtils.copyProperties(umsMemberReceiveAddress, addressResponseDTO);
        return addressResponseDTO;
    }
}
