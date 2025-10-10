package org.zhuyuqinlan.lemall.business.portal.member.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.portal.member.dto.request.UmsMemberReceiveAddressRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.UmsMemberReceiveAddressResponseDTO;
import org.zhuyuqinlan.lemall.business.portal.member.mapper.UmsMemberReceiveAddressMapper;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberReceiveAddress;
import org.zhuyuqinlan.lemall.business.portal.member.service.UmsMemberReceiveAddressService;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl extends ServiceImpl<UmsMemberReceiveAddressMapper, UmsMemberReceiveAddress> implements UmsMemberReceiveAddressService{

    @Override
    public boolean add(UmsMemberReceiveAddressRequestDTO address) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean updateAddress(Long id, UmsMemberReceiveAddressRequestDTO address) {
        return false;
    }

    @Override
    public List<UmsMemberReceiveAddressResponseDTO> listAddress() {
        return List.of();
    }

    @Override
    public UmsMemberReceiveAddressResponseDTO getItem(Long id) {
        return null;
    }
}
