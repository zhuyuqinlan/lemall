package org.zhuyuqinlan.lemall.common.web.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
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
    @Value("${redis.key.fs.access.local.localFileAccess}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;

    /**
     * 本地文件上传凭证key
     */
    @Value("${redis.key.fs.preUpload.local}")
    private String REDIS_KEY_PREUPLOAD_LOCAL;

    /**
     * token名
     **/
    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;


    @Operation(summary = "获取本地文件上传的access code")
    @GetMapping("/access-code")
    public Result<String> getAccessCode(HttpServletRequest request) {
        // 生成access码
        String accessCode = UUID.randomUUID().toString();
        // 存入redis
        String token = request.getHeader(SA_TOKEN_NAME);
        redisService.set(REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS.replace("{token}", token)
                        .replace("{accessCode}", accessCode),
                "1",
                FileStorageConstant.LOCAL_ACCESS_EXPIRE);
        // 返回结果
        return Result.success(accessCode);
    }

    @Operation(summary = "检查文件md5并获取上传凭证")
    @PostMapping("/check")
    public Result<FileInfoExistDTO> check(HttpServletRequest request, @RequestParam("md5") String md5, @RequestParam("accessCode") String accessCode) {
        // 校验access
        String token = request.getHeader(SA_TOKEN_NAME);
        if (!redisService.hasKey(REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode))) {
            return Result.fail("access已过期");
        }

        // 如果校验通过,删除access
        redisService.del(REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode));
        // 检查md5（秒传）
        FileInfoCacheByMd5DTO fileInfoByMd5 = fileCacheService.getFileInfoByMd5(md5);
        // 返回结果
        FileInfoExistDTO fileInfoExistDTO = new FileInfoExistDTO();
        if (fileInfoByMd5 == null) {
            // 生成文件上传凭证
            String uploadCode = UUID.randomUUID().toString();
            redisService.set(REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                    .replace("{token}", token).replace("{uploadCode}", uploadCode), md5,FileStorageConstant.LOCAL_UPLOAD_EXPIRE);
            fileInfoExistDTO.setExist(false);
            fileInfoExistDTO.setUploadCode(uploadCode);
            return Result.success(fileInfoExistDTO);
        } else {
            BeanUtils.copyProperties(fileInfoByMd5, fileInfoExistDTO);
            fileInfoExistDTO.setExist(true);
            return Result.success(fileInfoExistDTO);
        }
    }

    @Operation(summary = "上传文件（本地存储）")
    @PostMapping("/upload")
    public Result<FileInfoDTO> upload(@RequestParam("file") MultipartFile file, @RequestParam("uploadCode") String uploadCode ,HttpServletRequest request) {
        // 校验uploadCode
        String token = request.getHeader(SA_TOKEN_NAME);
        String md5 = redisService.get(REDIS_PREFIX + ":" + REDIS_KEY_PREUPLOAD_LOCAL
                .replace("{token}", token).replace("{uploadCode}", uploadCode));
        if (!StringUtils.hasText(md5)) {
            return Result.fail("uploadCode已过期");
        }

        try {
            // ====== 计算文件MD5 ======
            String fileMd5;
            try (InputStream in = file.getInputStream()) {
                fileMd5 = DigestUtils.md5DigestAsHex(in);
            }

            // ===== 校验文件 ======
            if (!md5.equals(fileMd5)) {
                return Result.fail("文件在上传过程中受损");
            }

            // ====== 判断文件是否已存在 ======
            FileInfoCacheByMd5DTO fileInfoByMd5 = fileCacheService.getFileInfoByMd5(fileMd5);
            if (fileInfoByMd5 != null) {
                // 文件已存在，直接返回
                FileInfoDTO fileInfoDTO = new FileInfoDTO();
                BeanUtils.copyProperties(fileInfoByMd5, fileInfoDTO);
                fileInfoDTO.setMd5(md5);
                return Result.success(fileInfoDTO);
            }

            // ====== 上传文件 ======
            String dateFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String newFileName = UUID.randomUUID() + suffix;
            String objectName = dateFolder + "/" + newFileName;

            try (InputStream inputStream = file.getInputStream()) {
                FileInfoDTO uploadInfo = fileStorageService.uploadFile(
                        objectName, inputStream, file.getSize(), file.getContentType(),fileMd5
                );

                return Result.success(uploadInfo);
            }

        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            return Result.fail("本地文件上传失败: " + e.getMessage());
        }
    }

}
