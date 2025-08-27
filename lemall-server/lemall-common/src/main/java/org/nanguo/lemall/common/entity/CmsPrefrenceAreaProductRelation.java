package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 优选专区和产品关系表
    */
@TableName(value = "cms_prefrence_area_product_relation")
public class CmsPrefrenceAreaProductRelation {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "prefrence_area_id")
    private Long prefrenceAreaId;

    @TableField(value = "product_id")
    private Long productId;

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
     * @return prefrence_area_id
     */
    public Long getPrefrenceAreaId() {
        return prefrenceAreaId;
    }

    /**
     * @param prefrenceAreaId
     */
    public void setPrefrenceAreaId(Long prefrenceAreaId) {
        this.prefrenceAreaId = prefrenceAreaId;
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
}