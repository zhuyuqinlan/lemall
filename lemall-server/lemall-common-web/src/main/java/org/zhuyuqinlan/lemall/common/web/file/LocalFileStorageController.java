package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.service.FileStorageService;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.zhuyuqinlan.lemall.common.service.RedisService;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "本地文件", description = "LocalFileStorageController")
@RequestMapping("${lemall.server.prefix.common}/file/local")
public class LocalFileStorageController {

    private final FileStorageService fileStorageService;
    private final RedisService redisService;

    public LocalFileStorageController(@Qualifier("localFileStorageService") FileStorageService fileStorageService, RedisService redisService) {
        this.fileStorageService = fileStorageService;
        this.redisService = redisService;
    }

    /** Redis 数据库前缀，用于隔离不同应用或业务 */
    @Value("${redis.database}")
    private String REDIS_DATABASE;

    /**
     * 本地文件access key前缀
     */
    @Value("${redis.key.localFileAccess}")
    private String REDIS_KEY_LOCAL_FILE_ACCESS;

    /** token名 **/
    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;


    @Operation(summary = "获取本地文件上传的access code（短期有效）")
    @GetMapping("/access-code")
    public Result<String> getAccessCode(HttpServletRequest request) {
        // 生成随机 access code
        String accessCode = UUID.randomUUID().toString();
        // 存入redis
        String token = request.getHeader(SA_TOKEN_NAME);
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_LOCAL_FILE_ACCESS + ":" + token, accessCode, FileStorageConstant.LOCAL_ACCESS_EXPIRE);
        return Result.success(accessCode);
    }

    @Operation(summary = "上传文件（本地存储）")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
        // 校验access
        String token = request.getHeader(SA_TOKEN_NAME);
        if (!redisService.hasKey(REDIS_DATABASE + ":" + REDIS_KEY_LOCAL_FILE_ACCESS + ":" + token)) {
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
