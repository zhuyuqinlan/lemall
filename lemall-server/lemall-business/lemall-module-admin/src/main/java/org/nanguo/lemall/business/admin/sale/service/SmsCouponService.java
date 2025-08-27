package org.nanguo.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.sale.dto.request.SmsCouponParamRequestDTO;
import org.nanguo.lemall.business.admin.sale.dto.response.SmsCouponResponseDTO;
import org.nanguo.lemall.common.entity.SmsCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SmsCouponService extends IService<SmsCoupon>{


    /**
     * 添加优惠券
     * @param couponParam 参数
     * @return 成功标志
     */
    boolean create(SmsCouponParamRequestDTO couponParam);

    /**
     * 删除优惠券
     * @param id id
     * @return 成功标志
     */
    boolean delete(Long id);

    /**
     * 修改优惠券
     * @param id id
     * @param couponParam 参数
     * @return 成功标志
     */
    boolean updateCoupon(Long id, SmsCouponParamRequestDTO couponParam);

    /**
     * 根据优惠券名称和类型分页获取优惠券列表
     * @param name 名称
     * @param type 类型
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    IPage<SmsCouponResponseDTO> listPage(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 获取单个优惠券的详细信息
     * @param id id
     * @return 结果
     */
    SmsCouponParamRequestDTO getItem(Long id);
}
