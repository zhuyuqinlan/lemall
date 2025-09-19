package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
    * 首页轮播广告表
    */
@TableName(value = "sms_home_advertise")
public class SmsHomeAdvertise {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    /**
     * 轮播位置：0->PC首页轮播；1->app首页轮播
     */
    @TableField(value = "`type`")
    private Integer type;

    @TableField(value = "pic")
    private String pic;

    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 上下线状态：0->下线；1->上线
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 点击数
     */
    @TableField(value = "click_count")
    private Integer clickCount;

    /**
     * 下单数
     */
    @TableField(value = "order_count")
    private Integer orderCount;

    /**
     * 链接地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 备注
     */
    @TableField(value = "note")
    private String note;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

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
     * 获取轮播位置：0->PC首页轮播；1->app首页轮播
     *
     * @return type - 轮播位置：0->PC首页轮播；1->app首页轮播
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置轮播位置：0->PC首页轮播；1->app首页轮播
     *
     * @param type 轮播位置：0->PC首页轮播；1->app首页轮播
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return pic
     */
    public String getPic() {
        return pic;
    }

    /**
     * @param pic
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * @return start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取上下线状态：0->下线；1->上线
     *
     * @return status - 上下线状态：0->下线；1->上线
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置上下线状态：0->下线；1->上线
     *
     * @param status 上下线状态：0->下线；1->上线
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取点击数
     *
     * @return click_count - 点击数
     */
    public Integer getClickCount() {
        return clickCount;
    }

    /**
     * 设置点击数
     *
     * @param clickCount 点击数
     */
    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    /**
     * 获取下单数
     *
     * @return order_count - 下单数
     */
    public Integer getOrderCount() {
        return orderCount;
    }

    /**
     * 设置下单数
     *
     * @param orderCount 下单数
     */
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    /**
     * 获取链接地址
     *
     * @return url - 链接地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置链接地址
     *
     * @param url 链接地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取备注
     *
     * @return note - 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注
     *
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}