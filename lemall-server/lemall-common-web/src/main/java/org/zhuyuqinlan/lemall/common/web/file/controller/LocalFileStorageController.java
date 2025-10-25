package org.zhuyuqinlan.lemall.common.web.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.file.service.FileCacheService;
import org.zhuyuqinlan.lemall.common.file.service.FileStorageService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@Tag(name = "本地文件", description = "LocalFileStorageController")
@RequestMapping("${lemall.server.prefix.common}/file/local")
public class LocalFileStorageController {

    private final FileStorageService fileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;

    public LocalFileStorageController(@Qualifier("localFileStorageService") FileStorageService fileStorageService, RedisService redisService, FileCacheService fileCacheService) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
    }

    /**
     * Redis 数据库前缀，用于隔离不同应用或业务
     */
    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;

    /**
     * 本地文件access key前缀
     */
    @Value("${redis.key.fs.access.localFileAccess}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;

    /**
     * token名
     **/
    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;


    @Operation(summary = "获取本地文件上传的access code（如果该文件在服务器上存在直接返回）")
    @GetMapping("/access-code")
    public Result<?> getAccessCode(@RequestParam("fileMd5") String fileMd5, HttpServletRequest request) {
        // 存入redis
        String token = request.getHeader(SA_TOKEN_NAME);
        redisService.set(REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS.replace("{token}", token),
                "1",
                FileStorageConstant.LOCAL_ACCESS_EXPIRE);
        // 检查redis中该md5是否存在
        FileInfoCacheByMd5DTO fileInfoByMd5 = fileCacheService.getFileInfoByMd5(fileMd5);
        // 返回结果
        Map<String,Object> map = new HashMap<>();
        if (fileInfoByMd5 == null) {
            map.put("exist",false);
        } else {
            map.put("exist",true);
            map.put("fileInfo",fileInfoByMd5);
        }
        return Result.success(map);
    }

    @Operation(summary = "上传文件（本地存储）")
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, HttpServletRequest request) {
        // 校验access
        String token = request.getHeader(SA_TOKEN_NAME);
        if (!redisService.hasKey(REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS.replace("{token}", token))) {
            return Result.fail("assess已key过期");
        }
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

            Map<String, String> stringStringMap =
                    fileStorageService.uploadFile(objectName, inputStream, size, contentType);
            return Result.success(stringStringMap);
        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            return Result.fail("本地文件上传失败: " + e.getMessage());
        }
    }
}
