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

    private final FileStorageService fileStorageService;

    public MinioFileStorageController(@Qualifier("minIOService") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "生成前端直传 POST Policy（仅云存储）")
    @GetMapping("/postPolicy")
    public Result<Map<String, Object>> postPolicy() {
        if (fileStorageService instanceof CloudFileStorageService cloudService) {
            try {
                String dateFolder = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                return Result.success(cloudService.getPostPolicy(dateFolder, FileStorageConstant.POST_POLICY_EXPIRE));
            } catch (Exception e) {
                log.error("生成 POST Policy 失败", e);
                return Result.fail("生成 POST Policy 失败: " + e.getMessage());
            }
        }
        return Result.fail("本存储类型不支持前端直传");
    }

    @Operation(summary = "前端直传完成回调")
    @PostMapping("/callback")
    public Result<Map<String, String>> uploadCallback(@RequestParam String originalFileName) {
        if (fileStorageService instanceof CloudFileStorageService cloudService) {
            try {
                Map<String, String> stringStringMap = cloudService.saveFileRecord(originalFileName);
                return Result.success(stringStringMap);
            } catch (Exception e) {
                return Result.fail("回调处理失败: " + e.getMessage());
            }
        }
        return Result.fail("本存储类型不支持前端直传回调");
    }
}
