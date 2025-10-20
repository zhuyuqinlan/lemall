package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.service.FileStorageService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@Tag(name = "本地文件", description = "LocalFileStorageController")
@RequestMapping("${lemall.server.prefix.common}/file/local")
public class LocalFileStorageController {

    private final FileStorageService fileStorageService;

    public LocalFileStorageController(@Qualifier("localFileStorageService") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "上传文件（本地存储）")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 日期目录，例如：20251020/
            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());

            // 取文件后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // 文件新名：UUID + 后缀
            String newFileName = UUID.randomUUID() + suffix;
            String objectName = dateFolder + "/" + newFileName;

            // 上传
            InputStream inputStream = file.getInputStream();
            long size = file.getSize();
            String contentType = file.getContentType();

            String url = fileStorageService.uploadFile(objectName, inputStream, size, contentType);
            return Result.success(url);
        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            return Result.fail("本地文件上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取文件访问 URL（本地存储）")
    @GetMapping("/url")
    public Result<String> getFileUrl(@RequestParam("objectName") String objectName) {
        try {
            return Result.success(fileStorageService.getFileUrl(objectName));
        } catch (Exception e) {
            log.error("获取文件 URL 失败", e);
            return Result.fail("获取文件 URL 失败: " + e.getMessage());
        }
    }
}
