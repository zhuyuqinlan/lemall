package org.zhuyuqinlan.lemall.business.portal.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "首页轮播广告响应DTO")
public class SmsHomeAdvertiseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "广告名称")
    private String name;

    @Schema(description = "轮播位置：0->PC首页轮播；1->app首页轮播")
    private Integer type;

    @Schema(description = "广告图片")
    private String pic;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "上下线状态：0->下线；1->上线")
    private Integer status;

    @Schema(description = "点击数")
    private Integer clickCount;

    @Schema(description = "下单数")
    private Integer orderCount;

    @Schema(description = "链接地址")
    private String url;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "排序")
    private Integer sort;
}

