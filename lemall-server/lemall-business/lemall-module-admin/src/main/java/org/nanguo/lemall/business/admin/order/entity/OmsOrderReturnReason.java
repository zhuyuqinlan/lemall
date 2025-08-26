package org.nanguo.lemall.business.admin.order.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
    * 退货原因表
    */
@TableName(value = "oms_order_return_reason")
public class OmsOrderReturnReason {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 退货类型
     */
    @TableField(value = "`name`")
    private String name;

    @TableField(value = "sort")
    private Integer sort;

    /**
     * 状态：0->不启用；1->启用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 添加时间
     */
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private Date createTime;

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
     * 获取退货类型
     *
     * @return name - 退货类型
     */
    public String getName() {
        return name;
    }

    /**
     * 设置退货类型
     *
     * @param name 退货类型
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取状态：0->不启用；1->启用
     *
     * @return status - 状态：0->不启用；1->启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0->不启用；1->启用
     *
     * @param status 状态：0->不启用；1->启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}