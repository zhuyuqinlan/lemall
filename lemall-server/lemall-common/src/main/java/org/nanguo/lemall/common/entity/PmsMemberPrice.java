package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

/**
 * 商品会员价格表
 */
@TableName(value = "pms_member_price")
public class PmsMemberPrice {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "product_id")
    private Long productId;

    @TableField(value = "member_level_id")
    private Long memberLevelId;

    /**
     * 会员价格
     */
    @TableField(value = "member_price")
    private BigDecimal memberPrice;

    @TableField(value = "member_level_name")
    private String memberLevelName;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return product_id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return member_level_id
     */
    public Long getMemberLevelId() {
        return memberLevelId;
    }

    /**
     * @param memberLevelId
     */
    public void setMemberLevelId(Long memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    /**
     * 获取会员价格
     *
     * @return member_price - 会员价格
     */
    public BigDecimal getMemberPrice() {
        return memberPrice;
    }

    /**
     * 设置会员价格
     *
     * @param memberPrice 会员价格
     */
    public void setMemberPrice(BigDecimal memberPrice) {
        this.memberPrice = memberPrice;
    }

    /**
     * @return member_level_name
     */
    public String getMemberLevelName() {
        return memberLevelName;
    }

    /**
     * @param memberLevelName
     */
    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }
}