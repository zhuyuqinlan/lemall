package org.zhuyuqinlan.lemall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
    * 会员收货地址表
    */
@TableName(value = "ums_member_receive_address")
public class UmsMemberReceiveAddress {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "member_id")
    private Long memberId;

    /**
     * 收货人名称
     */
    @TableField(value = "`name`")
    private String name;

    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 是否为默认
     */
    @TableField(value = "default_status")
    private Integer defaultStatus;

    /**
     * 邮政编码
     */
    @TableField(value = "post_code")
    private String postCode;

    /**
     * 省份/直辖市
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 区
     */
    @TableField(value = "region")
    private String region;

    /**
     * 详细地址(街道)
     */
    @TableField(value = "detail_address")
    private String detailAddress;

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
     * @return member_id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @param memberId
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取收货人名称
     *
     * @return name - 收货人名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置收货人名称
     *
     * @param name 收货人名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return phone_number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取是否为默认
     *
     * @return default_status - 是否为默认
     */
    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    /**
     * 设置是否为默认
     *
     * @param defaultStatus 是否为默认
     */
    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    /**
     * 获取邮政编码
     *
     * @return post_code - 邮政编码
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮政编码
     *
     * @param postCode 邮政编码
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 获取省份/直辖市
     *
     * @return province - 省份/直辖市
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份/直辖市
     *
     * @param province 省份/直辖市
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取区
     *
     * @return region - 区
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置区
     *
     * @param region 区
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 获取详细地址(街道)
     *
     * @return detail_address - 详细地址(街道)
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * 设置详细地址(街道)
     *
     * @param detailAddress 详细地址(街道)
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}