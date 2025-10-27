package org.zhuyuqinlan.lemall.common.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Schema(description = "上传凭证")
public class PostPolicyDTO {

    @Schema(description = "上传 endpoint 地址")
    private String url;

    @Schema(description = "表单数据，包含 key、policy、x-amz-signature 等")
    private Map<String, String> formData;

    @Schema(description = "过期时间戳（秒）")
    private long expire;

    @Schema(description = "对象名称（存储路径）")
    private String objectName;

    public PostPolicyDTO(String url, Map<String, String> formData, long expire, String objectName) {
        this.url = url;
        this.formData = formData;
        this.expire = expire;
        this.objectName = objectName;
    }
}
