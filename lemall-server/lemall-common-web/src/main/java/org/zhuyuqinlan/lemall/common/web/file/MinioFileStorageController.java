package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileStorageService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "minio文件", description = "FileStorageController")
@RequestMapping("${lemall.server.prefix.common}/file/minio")
public class MinioFileStorageController {

    private final CloudFileStorageService fileStorageService;

    public MinioFileStorageController(@Qualifier("minIOService") CloudFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "生成前端直传 POST Policy（仅云存储）")
    @GetMapping("/postPolicy")
    public Result<Map<String, Object>> postPolicy(@RequestParam String fileExt) {
        try {
            // 生成日期目录
            String dateFolder = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
            // 生成唯一文件名
            String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
            String objectName = String.format("%s/%s%s", dateFolder, uuid, fileExt);

            // 调用服务生成 POST Policy
            return Result.success(fileStorageService.getPostPolicy(objectName, FileStorageConstant.POST_POLICY_EXPIRE, true));
        } catch (Exception e) {
            log.error("生成 POST Policy 失败", e);
            return Result.fail("生成 POST Policy 失败: " + e.getMessage());
        }
    }

    @Operation(summary = "前端直传完成回调")
    @PostMapping("/callback")
    public Result<Map<String, String>> uploadCallback(@RequestParam String originalFileName) {
        try {
            Map<String, String> stringStringMap = fileStorageService.saveFileRecord(originalFileName, true);
            return Result.success(stringStringMap);
        } catch (Exception e) {
            return Result.fail("回调处理失败: " + e.getMessage());
        }
    }
}
