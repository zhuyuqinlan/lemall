package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoCacheByMd5DTO;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.dto.PostPolicyDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
import org.zhuyuqinlan.lemall.common.file.service.storage.CloudFileStorageService;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileCacheService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "minio文件", description = "FileStorageController")
@RequestMapping("${lemall.server.prefix.common}/file/minio")
public class MinioFileStorageController {

    private final CloudFileStorageService fileStorageService;
    private final RedisService redisService;
    private final FileCacheService fileCacheService;

    public MinioFileStorageController(@Qualifier("minIOService") CloudFileStorageService fileStorageService, RedisService redisService, FileCacheService fileCacheService) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
        this.fileCacheService = fileCacheService;
    }

    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;

    @Value("${redis.common-prefix}")
    private String REDIS_PREFIX;
    @Value("${redis.key.fs.access.minio.limit}")
    private String REDIS_KEY_MINIO_LIMIT_LIMIT;
    @Value("${redis.key.fs.access.minio.access}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;
    @Value("${redis.key.fs.preUpload.minio}")
    private String REDIS_KEY_MINIO_PREUPLOAD_MINIO;

    @GetMapping("/access-code")
    @Operation(summary = "获取上传 access code")
    public Result<String> accessCode(HttpServletRequest request) {
        String token = request.getHeader(SA_TOKEN_NAME);
        String limitKey = REDIS_PREFIX + ":" + REDIS_KEY_MINIO_LIMIT_LIMIT.replace("{token}", token);
        Long count = redisService.incr(limitKey, 1);
        if (count == 1) redisService.expire(limitKey, 60);
        if (count > 5) throw new RuntimeException("请求过于频繁，请稍后再试");

        String accessCode = UUID.randomUUID().toString();
        redisService.set(
                REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                        .replace("{token}", token)
                        .replace("{accessCode}", accessCode),
                "1",
                FileStorageConstant.MINIO_ACCESS_EXPIRE
        );
        return Result.success(accessCode);
    }

    @PostMapping("/check")
    @Operation(summary = "检查文件 MD5")
    public Result<FileInfoExistDTO> check(HttpServletRequest request, @RequestParam String md5,
                                @RequestParam String accessCode) {
        String token = request.getHeader(SA_TOKEN_NAME);
        String key = REDIS_PREFIX + ":" + REDIS_KEY_LOCAL_FILE_ACCESS
                .replace("{token}", token).replace("{accessCode}", accessCode);
        if (!redisService.hasKey(key)) throw new RuntimeException("access已过期");
        redisService.del(key);

        FileInfoCacheByMd5DTO cache = fileCacheService.getFileInfoByMd5(md5);
        FileInfoExistDTO dto = new FileInfoExistDTO();
        if (cache == null) {
            String uploadCode = UUID.randomUUID().toString();
            redisService.set(
                    REDIS_PREFIX + ":" + REDIS_KEY_MINIO_PREUPLOAD_MINIO
                            .replace("{token}", token)
                            .replace("{uploadCode}", uploadCode),
                    md5,
                    FileStorageConstant.LOCAL_UPLOAD_EXPIRE
            );
            dto.setExist(false);
            dto.setUploadCode(uploadCode);
        } else {
            BeanUtils.copyProperties(cache, dto);
            dto.setExist(true);
        }
        return Result.success(dto);
    }

    @Operation(summary = "生成前端直传凭证（公共桶）")
    @GetMapping("/postPolicy")
    public Result<PostPolicyDTO> postPolicy(@RequestParam String fileExt) {
        // 生成日期目录
        String dateFolder = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        // 生成唯一文件名
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        if (!fileExt.startsWith(".")) {
            fileExt = "." + fileExt;
        }
        String objectName = String.format("%s/%s%s", dateFolder, uuid, fileExt);
        // 调用服务生成 POST Policy
        return Result.success(fileStorageService.getPostPolicy(objectName, FileStorageConstant.POST_POLICY_EXPIRE, true));
    }

    @Operation(summary = "前端直传完成回调（公共桶）")
    @PostMapping("/callback")
    public Result<FileInfoDTO> uploadCallback(@RequestParam String originalFileName) {
        FileInfoDTO stringStringMap = fileStorageService.saveFileRecord(originalFileName, true);
        return Result.success(stringStringMap);
    }

    @Operation(summary = "前端分片上传凭证（公共桶）")
    @GetMapping("/getMultipart")
    public Result<MultipartUploadInfo> getMultipart(@RequestParam String fileExt, @RequestParam Long partSize,
                                                    @RequestParam long fileSize) {
        // 生成日期目录
        String dateFolder = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        // 生成唯一文件名
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        if (!fileExt.startsWith(".")) {
            fileExt = "." + fileExt;
        }
        String objectName = String.format("%s/%s%s", dateFolder, uuid, fileExt);
        MultipartUploadInfo multipartUploadInfo = fileStorageService.getMultipartUploadInfo(objectName, FileStorageConstant.POST_POLICY_MULTIPART_EXPIRE, partSize, fileSize, true);
        return Result.success(multipartUploadInfo);
    }

    @Operation(summary = "前端分片上传回调（公共桶）")
    @PostMapping("/callBackMultiPart")
    public Result<FileInfoDTO> callBackMultiPart() {
        return null;
    }


    @Operation(summary = "删除文件（minio公共桶）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/minio/delete")
    public Result<?> deleteFileMinio(@RequestParam("objectName") String objectName) {
        fileStorageService.deleteFile(objectName,true);
        return Result.success();
    }

    @Operation(summary = "上传文件（minio公共桶）")
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
            FileInfoDTO fileInfoDTO = fileStorageService.uploadFile(objectName, inputStream, size, contentType, fileMd5,false);
            return Result.success(fileInfoDTO);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return Result.fail("上传文件失败: " + e.getMessage());
        }
    }
}
