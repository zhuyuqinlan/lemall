package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 产品属性分类表
    */
@TableName(value = "pms_product_attribute_category")
public class PmsProductAttributeCategory {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    /**
     * 属性数量
     */
    @TableField(value = "attribute_count")
    private Integer attributeCount;

    /**
     * 参数数量
     */
    @TableField(value = "param_count")
    private Integer paramCount;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取属性数量
     *
     * @return attribute_count - 属性数量
     */
    public Integer getAttributeCount() {
        return attributeCount;
    }

    /**
     * 设置属性数量
     *
     * @param attributeCount 属性数量
     */
    public void setAttributeCount(Integer attributeCount) {
        this.attributeCount = attributeCount;
    }

    /**
     * 获取参数数量
     *
     * @return param_count - 参数数量
     */
    public Integer getParamCount() {
        return paramCount;
    }

    /**
     * 设置参数数量
     *
     * @param paramCount 参数数量
     */
    public void setParamCount(Integer paramCount) {
        this.paramCount = paramCount;
    }
}