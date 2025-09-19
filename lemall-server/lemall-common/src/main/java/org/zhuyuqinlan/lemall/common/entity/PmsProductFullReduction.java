package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

/**
    * 产品满减表(只针对同商品)
    */
@TableName(value = "pms_product_full_reduction")
public class PmsProductFullReduction {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "product_id")
    private Long productId;

    @TableField(value = "full_price")
    private BigDecimal fullPrice;

    @TableField(value = "reduce_price")
    private BigDecimal reducePrice;

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
     * @return full_price
     */
    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    /**
     * @param fullPrice
     */
    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    /**
     * @return reduce_price
     */
    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    /**
     * @param reducePrice
     */
    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }
}