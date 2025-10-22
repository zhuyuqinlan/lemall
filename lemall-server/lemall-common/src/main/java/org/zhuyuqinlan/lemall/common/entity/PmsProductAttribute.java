package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 商品属性参数表
    */
@TableName(value = "pms_product_attribute")
public class PmsProductAttribute {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "product_attribute_category_id")
    private Long productAttributeCategoryId;

    @TableField(value = "`name`")
    private String name;

    /**
     * 属性选择类型：0->唯一；1->单选；2->多选
     */
    @TableField(value = "select_type")
    private Integer selectType;

    /**
     * 属性录入方式：0->手工录入；1->从列表中选取
     */
    @TableField(value = "input_type")
    private Integer inputType;

    /**
     * 可选值列表，以逗号隔开
     */
    @TableField(value = "input_list")
    private String inputList;

    /**
     * 排序字段：最高的可以单独上传图片
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 分类筛选样式：1->普通；1->颜色
     */
    @TableField(value = "filter_type")
    private Integer filterType;

    /**
     * 检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
     */
    @TableField(value = "search_type")
    private Integer searchType;

    /**
     * 相同属性产品是否关联；0->不关联；1->关联
     */
    @TableField(value = "related_status")
    private Integer relatedStatus;

    /**
     * 是否支持手动新增；0->不支持；1->支持
     */
    @TableField(value = "hand_add_status")
    private Integer handAddStatus;

    /**
     * 属性的类型；0->规格；1->参数
     */
    @TableField(value = "`type`")
    private Integer type;

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
     * @return product_attribute_category_id
     */
    public Long getProductAttributeCategoryId() {
        return productAttributeCategoryId;
    }

    /**
     * @param productAttributeCategoryId
     */
    public void setProductAttributeCategoryId(Long productAttributeCategoryId) {
        this.productAttributeCategoryId = productAttributeCategoryId;
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
     * 获取属性选择类型：0->唯一；1->单选；2->多选
     *
     * @return select_type - 属性选择类型：0->唯一；1->单选；2->多选
     */
    public Integer getSelectType() {
        return selectType;
    }

    /**
     * 设置属性选择类型：0->唯一；1->单选；2->多选
     *
     * @param selectType 属性选择类型：0->唯一；1->单选；2->多选
     */
    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    /**
     * 获取属性录入方式：0->手工录入；1->从列表中选取
     *
     * @return input_type - 属性录入方式：0->手工录入；1->从列表中选取
     */
    public Integer getInputType() {
        return inputType;
    }

    /**
     * 设置属性录入方式：0->手工录入；1->从列表中选取
     *
     * @param inputType 属性录入方式：0->手工录入；1->从列表中选取
     */
    public void setInputType(Integer inputType) {
        this.inputType = inputType;
    }

    /**
     * 获取可选值列表，以逗号隔开
     *
     * @return input_list - 可选值列表，以逗号隔开
     */
    public String getInputList() {
        return inputList;
    }

    /**
     * 设置可选值列表，以逗号隔开
     *
     * @param inputList 可选值列表，以逗号隔开
     */
    public void setInputList(String inputList) {
        this.inputList = inputList;
    }

    /**
     * 获取排序字段：最高的可以单独上传图片
     *
     * @return sort - 排序字段：最高的可以单独上传图片
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序字段：最高的可以单独上传图片
     *
     * @param sort 排序字段：最高的可以单独上传图片
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取分类筛选样式：1->普通；1->颜色
     *
     * @return filter_type - 分类筛选样式：1->普通；1->颜色
     */
    public Integer getFilterType() {
        return filterType;
    }

    /**
     * 设置分类筛选样式：1->普通；1->颜色
     *
     * @param filterType 分类筛选样式：1->普通；1->颜色
     */
    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    /**
     * 获取检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
     *
     * @return search_type - 检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
     */
    public Integer getSearchType() {
        return searchType;
    }

    /**
     * 设置检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
     *
     * @param searchType 检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
     */
    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    /**
     * 获取相同属性产品是否关联；0->不关联；1->关联
     *
     * @return related_status - 相同属性产品是否关联；0->不关联；1->关联
     */
    public Integer getRelatedStatus() {
        return relatedStatus;
    }

    /**
     * 设置相同属性产品是否关联；0->不关联；1->关联
     *
     * @param relatedStatus 相同属性产品是否关联；0->不关联；1->关联
     */
    public void setRelatedStatus(Integer relatedStatus) {
        this.relatedStatus = relatedStatus;
    }

    /**
     * 获取是否支持手动新增；0->不支持；1->支持
     *
     * @return hand_add_status - 是否支持手动新增；0->不支持；1->支持
     */
    public Integer getHandAddStatus() {
        return handAddStatus;
    }

    /**
     * 设置是否支持手动新增；0->不支持；1->支持
     *
     * @param handAddStatus 是否支持手动新增；0->不支持；1->支持
     */
    public void setHandAddStatus(Integer handAddStatus) {
        this.handAddStatus = handAddStatus;
    }

    /**
     * 获取属性的类型；0->规格；1->参数
     *
     * @return type - 属性的类型；0->规格；1->参数
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置属性的类型；0->规格；1->参数
     *
     * @param type 属性的类型；0->规格；1->参数
     */
    public void setType(Integer type) {
        this.type = type;
    }
}