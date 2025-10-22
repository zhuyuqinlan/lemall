package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.service.FileStorageService;
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

    private final FileStorageService localFileStorageService;
    private final FileStorageService minioFileStorageService;

    public DevFileController(@Qualifier("localFileStorageService") FileStorageService localFileStorageService, @Qualifier("minIOService") FileStorageService minioFileStorageService) {
        this.localFileStorageService = localFileStorageService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Value("${localFile.local-download-prefix}")
    private String localFileDownloadPrefix;

    /**
     * 浏览器访问或下载文件（本地存储）
     */
    @GetMapping("${localFile.local-download-prefix}/**")
    public void serveFile(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String objectName = requestURI.replaceFirst("^" + localFileDownloadPrefix + "/", "");
        System.out.println(objectName);
        try (InputStream inputStream = localFileStorageService.downloadFile(objectName)) {
            // 自动识别文件类型
            Path path = Paths.get(objectName);
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            // 浏览器直接预览，不加 attachment
            // 如果强制下载，加上 Content-Disposition
            String filename = URLEncoder.encode(path.getFileName().toString(), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "inline; filename=" + filename);

            inputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            log.error("读取本地文件失败", e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Operation(summary = "删除文件（本地存储）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/local/delete")
    public Result<?> deleteFileLocal(@RequestParam("objectName") String objectName) {
        try {
            localFileStorageService.deleteFile(objectName);
            return Result.success();
        } catch (Exception e) {
            log.error("删除本地文件失败", e);
            return Result.fail("删除本地文件失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除文件（Minio）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/minio/delete")
    public Result<?> deleteFileMinio(@RequestParam("objectName") String objectName) {
        try {
            minioFileStorageService.deleteFile(objectName);
            return Result.success();
        } catch (Exception e) {
            log.error("删除minio文件失败", e);
            return Result.fail("删除minio文件失败: " + e.getMessage());
        }
    }

    @Operation(summary = "上传文件")
    @PostMapping("${lemall.server.prefix.common}/file/dev/minio/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
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

            // 上传
            String url = minioFileStorageService.uploadFile(objectName, inputStream, size, contentType);
            return Result.success(url);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return Result.fail("上传文件失败: " + e.getMessage());
        }
    }
}
