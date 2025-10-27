package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.service.biz.LocalFileService;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileStorageService;
import org.zhuyuqinlan.lemall.common.response.Result;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Profile("dev")  // 只在 dev 环境生效
@Slf4j
@RestController
@Tag(name = "文件操作(开发环境专用)", description = "LocalFileStorageController")
public class DevFileController {

    private final LocalFileService localFileService;
    private final CloudFileStorageService minioFileStorageService;

    public DevFileController(LocalFileService localFileService,
                             @Qualifier("minIOService") CloudFileStorageService minioFileStorageService) {
        this.localFileService = localFileService;
        this.minioFileStorageService = minioFileStorageService;
    }

    /**
     * 浏览器访问或下载文件（本地存储）
     */
    @GetMapping("${localFile.local-download-prefix}/**")
    public void serveFile(HttpServletRequest request, HttpServletResponse response) {
        localFileService.serveFile(request.getRequestURI(), response);
    }

    @PostMapping("${lemall.server.prefix.common}/file/dev/local/delete")
    public Result<?> deleteFileLocal(@RequestParam("objectName") String objectName) {
        localFileService.deleteFile(objectName);
        return Result.success();
    }

    @Operation(summary = "删除文件（Minio）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/minio/delete")
    public Result<?> deleteFileMinio(@RequestParam("objectName") String objectName) {
        try {
            minioFileStorageService.deleteFile(objectName,false);
            return Result.success();
        } catch (Exception e) {
            log.error("删除minio文件失败", e);
            return Result.fail("删除minio文件失败: " + e.getMessage());
        }
    }

    @Operation(summary = "上传文件（minio）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/minio/upload")
    public Result<FileInfoDTO> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 生成当天文件夹名
            String dateFolder = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());

            // 获取原文件名后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // 用 UUID 重命名文件
            String newFileName = java.util.UUID.randomUUID() + suffix;

            // 拼接文件路径：日期文件夹 + 文件名
            String objectName = dateFolder + "/" + newFileName;

            InputStream inputStream = file.getInputStream();
            long size = file.getSize();
            String contentType = file.getContentType();

            String fileMd5;
            try (InputStream in = file.getInputStream()) {
                fileMd5 = DigestUtils.md5DigestAsHex(in);
            }
            // 上传
            FileInfoDTO fileInfoDTO = minioFileStorageService.uploadFile(objectName, inputStream, size, contentType, fileMd5,false);
            return Result.success(fileInfoDTO);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return Result.fail("上传文件失败: " + e.getMessage());
        }
    }
}
