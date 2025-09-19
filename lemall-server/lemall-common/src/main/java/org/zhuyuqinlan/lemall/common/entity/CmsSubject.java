package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
    * 专题表
    */
@TableName(value = "cms_subject")
public class CmsSubject {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "category_id")
    private Long categoryId;

    @TableField(value = "title")
    private String title;

    /**
     * 专题主图
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 关联产品数量
     */
    @TableField(value = "product_count")
    private Integer productCount;

    @TableField(value = "recommend_status")
    private Integer recommendStatus;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "collect_count")
    private Integer collectCount;

    @TableField(value = "read_count")
    private Integer readCount;

    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 画册图片用逗号分割
     */
    @TableField(value = "album_pics")
    private String albumPics;

    @TableField(value = "description")
    private String description;

    /**
     * 显示状态：0->不显示；1->显示
     */
    @TableField(value = "show_status")
    private Integer showStatus;

    @TableField(value = "content")
    private String content;

    /**
     * 转发数
     */
    @TableField(value = "forward_count")
    private Integer forwardCount;

    /**
     * 专题分类名称
     */
    @TableField(value = "category_name")
    private String categoryName;

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
     * @return category_id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取专题主图
     *
     * @return pic - 专题主图
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置专题主图
     *
     * @param pic 专题主图
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 获取关联产品数量
     *
     * @return product_count - 关联产品数量
     */
    public Integer getProductCount() {
        return productCount;
    }

    /**
     * 设置关联产品数量
     *
     * @param productCount 关联产品数量
     */
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    /**
     * @return recommend_status
     */
    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    /**
     * @param recommendStatus
     */
    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return collect_count
     */
    public Integer getCollectCount() {
        return collectCount;
    }

    /**
     * @param collectCount
     */
    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    /**
     * @return read_count
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * @param readCount
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    /**
     * @return comment_count
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * 获取画册图片用逗号分割
     *
     * @return album_pics - 画册图片用逗号分割
     */
    public String getAlbumPics() {
        return albumPics;
    }

    /**
     * 设置画册图片用逗号分割
     *
     * @param albumPics 画册图片用逗号分割
     */
    public void setAlbumPics(String albumPics) {
        this.albumPics = albumPics;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取显示状态：0->不显示；1->显示
     *
     * @return show_status - 显示状态：0->不显示；1->显示
     */
    public Integer getShowStatus() {
        return showStatus;
    }

    /**
     * 设置显示状态：0->不显示；1->显示
     *
     * @param showStatus 显示状态：0->不显示；1->显示
     */
    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取转发数
     *
     * @return forward_count - 转发数
     */
    public Integer getForwardCount() {
        return forwardCount;
    }

    /**
     * 设置转发数
     *
     * @param forwardCount 转发数
     */
    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }

    /**
     * 获取专题分类名称
     *
     * @return category_name - 专题分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置专题分类名称
     *
     * @param categoryName 专题分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}