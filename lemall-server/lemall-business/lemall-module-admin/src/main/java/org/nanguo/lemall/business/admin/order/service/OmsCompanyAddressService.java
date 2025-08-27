package org.nanguo.lemall.business.admin.order.service;

import org.nanguo.lemall.business.admin.order.dto.response.OmsCompanyAddressResponseDTO;
import org.nanguo.lemall.common.entity.OmsCompanyAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OmsCompanyAddressService extends IService<OmsCompanyAddress>{


    /**
     * 获取所有收货地址
     * @return 结果
     */
    List<OmsCompanyAddressResponseDTO> listAll();
}
