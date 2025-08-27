package org.nanguo.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

/**
    * 商品信息
    */
@TableName(value = "pms_product")
public class PmsProduct {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "brand_id")
    private Long brandId;

    @TableField(value = "product_category_id")
    private Long productCategoryId;

    @TableField(value = "feight_template_id")
    private Long feightTemplateId;

    @TableField(value = "product_attribute_category_id")
    private Long productAttributeCategoryId;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "pic")
    private String pic;

    /**
     * 货号
     */
    @TableField(value = "product_sn")
    private String productSn;

    /**
     * 删除状态：0->未删除；1->已删除
     */
    @TableField(value = "delete_status")
    private Integer deleteStatus;

    /**
     * 上架状态：0->下架；1->上架
     */
    @TableField(value = "publish_status")
    private Integer publishStatus;

    /**
     * 新品状态:0->不是新品；1->新品
     */
    @TableField(value = "new_status")
    private Integer newStatus;

    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    @TableField(value = "recommand_status")
    private Integer recommandStatus;

    /**
     * 审核状态：0->未审核；1->审核通过
     */
    @TableField(value = "verify_status")
    private Integer verifyStatus;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 销量
     */
    @TableField(value = "sale")
    private Integer sale;

    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 促销价格
     */
    @TableField(value = "promotion_price")
    private BigDecimal promotionPrice;

    /**
     * 赠送的成长值
     */
    @TableField(value = "gift_growth")
    private Integer giftGrowth;

    /**
     * 赠送的积分
     */
    @TableField(value = "gift_point")
    private Integer giftPoint;

    /**
     * 限制使用的积分数
     */
    @TableField(value = "use_point_limit")
    private Integer usePointLimit;

    /**
     * 副标题
     */
    @TableField(value = "sub_title")
    private String subTitle;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 市场价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 库存预警值
     */
    @TableField(value = "low_stock")
    private Integer lowStock;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 商品重量，默认为克
     */
    @TableField(value = "weight")
    private BigDecimal weight;

    /**
     * 是否为预告商品：0->不是；1->是
     */
    @TableField(value = "preview_status")
    private Integer previewStatus;

    /**
     * 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     */
    @TableField(value = "service_ids")
    private String serviceIds;

    @TableField(value = "keywords")
    private String keywords;

    @TableField(value = "note")
    private String note;

    /**
     * 画册图片，连产品图片限制为5张，以逗号分割
     */
    @TableField(value = "album_pics")
    private String albumPics;

    @TableField(value = "detail_title")
    private String detailTitle;

    @TableField(value = "detail_desc")
    private String detailDesc;

    /**
     * 产品详情网页内容
     */
    @TableField(value = "detail_html")
    private String detailHtml;

    /**
     * 移动端网页详情
     */
    @TableField(value = "detail_mobile_html")
    private String detailMobileHtml;

    /**
     * 促销开始时间
     */
    @TableField(value = "promotion_start_time")
    private Date promotionStartTime;

    /**
     * 促销结束时间
     */
    @TableField(value = "promotion_end_time")
    private Date promotionEndTime;

    /**
     * 活动限购数量
     */
    @TableField(value = "promotion_per_limit")
    private Integer promotionPerLimit;

    /**
     * 促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
     */
    @TableField(value = "promotion_type")
    private Integer promotionType;

    /**
     * 品牌名称
     */
    @TableField(value = "brand_name")
    private String brandName;

    /**
     * 商品分类名称
     */
    @TableField(value = "product_category_name")
    private String productCategoryName;

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
     * @return brand_id
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @param brandId
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * @return product_category_id
     */
    public Long getProductCategoryId() {
        return productCategoryId;
    }

    /**
     * @param productCategoryId
     */
    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    /**
     * @return feight_template_id
     */
    public Long getFeightTemplateId() {
        return feightTemplateId;
    }

    /**
     * @param feightTemplateId
     */
    public void setFeightTemplateId(Long feightTemplateId) {
        this.feightTemplateId = feightTemplateId;
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
     * 获取货号
     *
     * @return product_sn - 货号
     */
    public String getProductSn() {
        return productSn;
    }

    /**
     * 设置货号
     *
     * @param productSn 货号
     */
    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    /**
     * 获取删除状态：0->未删除；1->已删除
     *
     * @return delete_status - 删除状态：0->未删除；1->已删除
     */
    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置删除状态：0->未删除；1->已删除
     *
     * @param deleteStatus 删除状态：0->未删除；1->已删除
     */
    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    /**
     * 获取上架状态：0->下架；1->上架
     *
     * @return publish_status - 上架状态：0->下架；1->上架
     */
    public Integer getPublishStatus() {
        return publishStatus;
    }

    /**
     * 设置上架状态：0->下架；1->上架
     *
     * @param publishStatus 上架状态：0->下架；1->上架
     */
    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    /**
     * 获取新品状态:0->不是新品；1->新品
     *
     * @return new_status - 新品状态:0->不是新品；1->新品
     */
    public Integer getNewStatus() {
        return newStatus;
    }

    /**
     * 设置新品状态:0->不是新品；1->新品
     *
     * @param newStatus 新品状态:0->不是新品；1->新品
     */
    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * 获取推荐状态；0->不推荐；1->推荐
     *
     * @return recommand_status - 推荐状态；0->不推荐；1->推荐
     */
    public Integer getRecommandStatus() {
        return recommandStatus;
    }

    /**
     * 设置推荐状态；0->不推荐；1->推荐
     *
     * @param recommandStatus 推荐状态；0->不推荐；1->推荐
     */
    public void setRecommandStatus(Integer recommandStatus) {
        this.recommandStatus = recommandStatus;
    }

    /**
     * 获取审核状态：0->未审核；1->审核通过
     *
     * @return verify_status - 审核状态：0->未审核；1->审核通过
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * 设置审核状态：0->未审核；1->审核通过
     *
     * @param verifyStatus 审核状态：0->未审核；1->审核通过
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
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

    /**
     * 获取销量
     *
     * @return sale - 销量
     */
    public Integer getSale() {
        return sale;
    }

    /**
     * 设置销量
     *
     * @param sale 销量
     */
    public void setSale(Integer sale) {
        this.sale = sale;
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取促销价格
     *
     * @return promotion_price - 促销价格
     */
    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    /**
     * 设置促销价格
     *
     * @param promotionPrice 促销价格
     */
    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    /**
     * 获取赠送的成长值
     *
     * @return gift_growth - 赠送的成长值
     */
    public Integer getGiftGrowth() {
        return giftGrowth;
    }

    /**
     * 设置赠送的成长值
     *
     * @param giftGrowth 赠送的成长值
     */
    public void setGiftGrowth(Integer giftGrowth) {
        this.giftGrowth = giftGrowth;
    }

    /**
     * 获取赠送的积分
     *
     * @return gift_point - 赠送的积分
     */
    public Integer getGiftPoint() {
        return giftPoint;
    }

    /**
     * 设置赠送的积分
     *
     * @param giftPoint 赠送的积分
     */
    public void setGiftPoint(Integer giftPoint) {
        this.giftPoint = giftPoint;
    }

    /**
     * 获取限制使用的积分数
     *
     * @return use_point_limit - 限制使用的积分数
     */
    public Integer getUsePointLimit() {
        return usePointLimit;
    }

    /**
     * 设置限制使用的积分数
     *
     * @param usePointLimit 限制使用的积分数
     */
    public void setUsePointLimit(Integer usePointLimit) {
        this.usePointLimit = usePointLimit;
    }

    /**
     * 获取副标题
     *
     * @return sub_title - 副标题
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * 设置副标题
     *
     * @param subTitle 副标题
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * 获取商品描述
     *
     * @return description - 商品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     *
     * @param description 商品描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取市场价
     *
     * @return original_price - 市场价
     */
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    /**
     * 设置市场价
     *
     * @param originalPrice 市场价
     */
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * 获取库存
     *
     * @return stock - 库存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置库存
     *
     * @param stock 库存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取库存预警值
     *
     * @return low_stock - 库存预警值
     */
    public Integer getLowStock() {
        return lowStock;
    }

    /**
     * 设置库存预警值
     *
     * @param lowStock 库存预警值
     */
    public void setLowStock(Integer lowStock) {
        this.lowStock = lowStock;
    }

    /**
     * 获取单位
     *
     * @return unit - 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取商品重量，默认为克
     *
     * @return weight - 商品重量，默认为克
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * 设置商品重量，默认为克
     *
     * @param weight 商品重量，默认为克
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * 获取是否为预告商品：0->不是；1->是
     *
     * @return preview_status - 是否为预告商品：0->不是；1->是
     */
    public Integer getPreviewStatus() {
        return previewStatus;
    }

    /**
     * 设置是否为预告商品：0->不是；1->是
     *
     * @param previewStatus 是否为预告商品：0->不是；1->是
     */
    public void setPreviewStatus(Integer previewStatus) {
        this.previewStatus = previewStatus;
    }

    /**
     * 获取以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     *
     * @return service_ids - 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     */
    public String getServiceIds() {
        return serviceIds;
    }

    /**
     * 设置以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     *
     * @param serviceIds 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
     */
    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    /**
     * @return keywords
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取画册图片，连产品图片限制为5张，以逗号分割
     *
     * @return album_pics - 画册图片，连产品图片限制为5张，以逗号分割
     */
    public String getAlbumPics() {
        return albumPics;
    }

    /**
     * 设置画册图片，连产品图片限制为5张，以逗号分割
     *
     * @param albumPics 画册图片，连产品图片限制为5张，以逗号分割
     */
    public void setAlbumPics(String albumPics) {
        this.albumPics = albumPics;
    }

    /**
     * @return detail_title
     */
    public String getDetailTitle() {
        return detailTitle;
    }

    /**
     * @param detailTitle
     */
    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    /**
     * @return detail_desc
     */
    public String getDetailDesc() {
        return detailDesc;
    }

    /**
     * @param detailDesc
     */
    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    /**
     * 获取产品详情网页内容
     *
     * @return detail_html - 产品详情网页内容
     */
    public String getDetailHtml() {
        return detailHtml;
    }

    /**
     * 设置产品详情网页内容
     *
     * @param detailHtml 产品详情网页内容
     */
    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    /**
     * 获取移动端网页详情
     *
     * @return detail_mobile_html - 移动端网页详情
     */
    public String getDetailMobileHtml() {
        return detailMobileHtml;
    }

    /**
     * 设置移动端网页详情
     *
     * @param detailMobileHtml 移动端网页详情
     */
    public void setDetailMobileHtml(String detailMobileHtml) {
        this.detailMobileHtml = detailMobileHtml;
    }

    /**
     * 获取促销开始时间
     *
     * @return promotion_start_time - 促销开始时间
     */
    public Date getPromotionStartTime() {
        return promotionStartTime;
    }

    /**
     * 设置促销开始时间
     *
     * @param promotionStartTime 促销开始时间
     */
    public void setPromotionStartTime(Date promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    /**
     * 获取促销结束时间
     *
     * @return promotion_end_time - 促销结束时间
     */
    public Date getPromotionEndTime() {
        return promotionEndTime;
    }

    /**
     * 设置促销结束时间
     *
     * @param promotionEndTime 促销结束时间
     */
    public void setPromotionEndTime(Date promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    /**
     * 获取活动限购数量
     *
     * @return promotion_per_limit - 活动限购数量
     */
    public Integer getPromotionPerLimit() {
        return promotionPerLimit;
    }

    /**
     * 设置活动限购数量
     *
     * @param promotionPerLimit 活动限购数量
     */
    public void setPromotionPerLimit(Integer promotionPerLimit) {
        this.promotionPerLimit = promotionPerLimit;
    }

    /**
     * 获取促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
     *
     * @return promotion_type - 促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
     */
    public Integer getPromotionType() {
        return promotionType;
    }

    /**
     * 设置促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
     *
     * @param promotionType 促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
     */
    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    /**
     * 获取品牌名称
     *
     * @return brand_name - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 获取商品分类名称
     *
     * @return product_category_name - 商品分类名称
     */
    public String getProductCategoryName() {
        return productCategoryName;
    }

    /**
     * 设置商品分类名称
     *
     * @param productCategoryName 商品分类名称
     */
    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }
}