package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 品牌表
    */
@TableName(value = "pms_brand")
public class PmsBrand {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    /**
     * 首字母
     */
    @TableField(value = "first_letter")
    private String firstLetter;

    @TableField(value = "sort")
    private Integer sort;

    /**
     * 是否为品牌制造商：0->不是；1->是
     */
    @TableField(value = "factory_status")
    private Integer factoryStatus;

    @TableField(value = "show_status")
    private Integer showStatus;

    /**
     * 产品数量
     */
    @TableField(value = "product_count")
    private Integer productCount;

    /**
     * 产品评论数量
     */
    @TableField(value = "product_comment_count")
    private Integer productCommentCount;

    /**
     * 品牌logo
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 专区大图
     */
    @TableField(value = "big_pic")
    private String bigPic;

    /**
     * 品牌故事
     */
    @TableField(value = "brand_story")
    private String brandStory;

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
     * 获取首字母
     *
     * @return first_letter - 首字母
     */
    public String getFirstLetter() {
        return firstLetter;
    }

    /**
     * 设置首字母
     *
     * @param firstLetter 首字母
     */
    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
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
     * 获取是否为品牌制造商：0->不是；1->是
     *
     * @return factory_status - 是否为品牌制造商：0->不是；1->是
     */
    public Integer getFactoryStatus() {
        return factoryStatus;
    }

    /**
     * 设置是否为品牌制造商：0->不是；1->是
     *
     * @param factoryStatus 是否为品牌制造商：0->不是；1->是
     */
    public void setFactoryStatus(Integer factoryStatus) {
        this.factoryStatus = factoryStatus;
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

    /**
     * 获取产品数量
     *
     * @return product_count - 产品数量
     */
    public Integer getProductCount() {
        return productCount;
    }

    /**
     * 设置产品数量
     *
     * @param productCount 产品数量
     */
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    /**
     * 获取产品评论数量
     *
     * @return product_comment_count - 产品评论数量
     */
    public Integer getProductCommentCount() {
        return productCommentCount;
    }

    /**
     * 设置产品评论数量
     *
     * @param productCommentCount 产品评论数量
     */
    public void setProductCommentCount(Integer productCommentCount) {
        this.productCommentCount = productCommentCount;
    }

    /**
     * 获取品牌logo
     *
     * @return logo - 品牌logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置品牌logo
     *
     * @param logo 品牌logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 获取专区大图
     *
     * @return big_pic - 专区大图
     */
    public String getBigPic() {
        return bigPic;
    }

    /**
     * 设置专区大图
     *
     * @param bigPic 专区大图
     */
    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    /**
     * 获取品牌故事
     *
     * @return brand_story - 品牌故事
     */
    public String getBrandStory() {
        return brandStory;
    }

    /**
     * 设置品牌故事
     *
     * @param brandStory 品牌故事
     */
    public void setBrandStory(String brandStory) {
        this.brandStory = brandStory;
    }
}