package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 优选专区
    */
@TableName(value = "cms_prefrence_area")
public class CmsPrefrenceArea {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "sub_title")
    private String subTitle;

    /**
     * 展示图片
     */
    @TableField(value = "pic")
    private byte[] pic;

    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "show_status")
    private Integer showStatus;

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
     * @return sub_title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * 获取展示图片
     *
     * @return pic - 展示图片
     */
    public byte[] getPic() {
        return pic;
    }

    /**
     * 设置展示图片
     *
     * @param pic 展示图片
     */
    public void setPic(byte[] pic) {
        this.pic = pic;
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
     * @return show_status
     */
    public Integer getShowStatus() {
        return showStatus;
    }

    /**
     * @param showStatus
     */
    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }
}