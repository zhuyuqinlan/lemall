package org.zhuyuqinlan.lemall.business.portal.member.service;

import org.zhuyuqinlan.lemall.business.portal.member.dto.request.UmsMemberReceiveAddressRequestDTO;
import org.zhuyuqinlan.lemall.business.portal.member.dto.response.UmsMemberReceiveAddressResponseDTO;
import org.zhuyuqinlan.lemall.common.entity.UmsMemberReceiveAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UmsMemberReceiveAddressService extends IService<UmsMemberReceiveAddress>{


    /**
     * 添加收货地址
     */
    boolean add(UmsMemberReceiveAddressRequestDTO address);

    /**
     * 删除收货地址
     * @param id 地址表的id
     */
    boolean delete(Long id);

    /**
     * 修改收货地址
     * @param id 地址表的id
     * @param address 修改的收货地址信息
     */
    boolean updateAddress(Long id, UmsMemberReceiveAddressRequestDTO address);

    /**
     * 返回当前用户的收货地址
     */
    List<UmsMemberReceiveAddressResponseDTO> listAddress();

    /**
     * 获取地址详情
     * @param id 地址id
     */
    UmsMemberReceiveAddressResponseDTO getItem(Long id);
}
