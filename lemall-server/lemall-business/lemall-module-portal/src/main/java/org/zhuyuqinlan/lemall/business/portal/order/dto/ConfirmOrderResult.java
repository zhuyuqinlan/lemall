package org.zhuyuqinlan.lemall.business.portal.order.dto;

import lombok.Getter;
import lombok.Setter;
import org.zhuyuqinlan.lemall.business.portal.member.dto.CartPromotionItem;
import org.zhuyuqinlan.lemall.business.portal.member.dto.SmsCouponHistoryDetail;
import org.zhuyuqinlan.lemall.business.portal.member.dto.UmsMemberReceiveAddressDTO;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ConfirmOrderResult {
    //包含优惠信息的购物车信息
    private List<CartPromotionItem> cartPromotionItemList;
    //用户收货地址列表
    private List<UmsMemberReceiveAddressDTO> memberReceiveAddressList;
    //用户可用优惠券列表
    private List<SmsCouponHistoryDetail> couponHistoryDetailList;
    //积分使用规则
    private UmsIntegrationConsumeSettingDTO integrationConsumeSetting;
    //会员持有的积分
    private Integer memberIntegration;
    //计算的金额
    private CalcAmount calcAmount;

    @Getter
    @Setter
    public static class CalcAmount {
        //订单商品总金额
        private BigDecimal totalAmount;
        //运费
        private BigDecimal freightAmount;
        //活动优惠
        private BigDecimal promotionAmount;
        //应付金额
        private BigDecimal payAmount;
    }
}
