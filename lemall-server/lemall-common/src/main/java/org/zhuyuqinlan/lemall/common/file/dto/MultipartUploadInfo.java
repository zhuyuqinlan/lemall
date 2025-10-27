package org.zhuyuqinlan.lemall.common.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(description = "分片凭证")
public class MultipartUploadInfo {

    @Schema(description = "分片列表，每个分片包含 partNumber 和 url")
    private List<Map<String, Object>> parts;

    @Schema(description = "总分片数")
    private int partCount;

    public MultipartUploadInfo(List<Map<String, Object>> parts, int partCount) {
        this.parts = parts;
        this.partCount = partCount;
    }
}