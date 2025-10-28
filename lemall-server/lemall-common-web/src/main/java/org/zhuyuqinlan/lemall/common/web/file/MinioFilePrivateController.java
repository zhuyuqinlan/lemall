package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;
import org.zhuyuqinlan.lemall.common.file.service.biz.MinioFileService;
import org.zhuyuqinlan.lemall.common.response.Result;

@Slf4j
@RestController
@RequestMapping("${lemall.server.prefix.common}/file/minio-private")
@RequiredArgsConstructor
@Tag(name = "minio私有桶")
public class MinioFilePrivateController {

    private final MinioFileService minioFileService;

    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;

    @GetMapping("/access-code")
    @Operation(summary = "获取上传accessCode")
    public Result<String> accessCode(HttpServletRequest request) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(minioFileService.accessCode(token, false));
    }

    @PostMapping("/upload-url")
    @Operation(summary = "上传凭证(单文件)")
    public Result<FileInfoExistDTO> uploadUrl(HttpServletRequest request,
                                              @RequestParam String accessCode,
                                              @RequestParam String md5) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(minioFileService.uploadUrl(token, accessCode, md5, false));
    }

    @PostMapping("/complete")
    @Operation(summary = "上传完回调(单文件)")
    public Result<FileInfoDTO> complete(HttpServletRequest request, @RequestParam String uploadId) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(minioFileService.complete(token, uploadId, false));
    }

    @PostMapping("/upload-url-multipart")
    @Operation(summary = "上传凭证(分片)")
    public Result<MultipartUploadInfo> multipartUploadInfoResult(HttpServletRequest request,
                                                                 @RequestParam String accessCode,
                                                                 @RequestParam String md5) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(minioFileService.multipartUploadInfoResult(token, accessCode, md5, false));
    }

    @PostMapping("/complete-multipart")
    @Operation(summary = "上传完回调(分片)")
    public Result<FileInfoDTO> completeMultipart(HttpServletRequest request, @RequestParam String uploadId) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(minioFileService.completeMultipart(token, uploadId, false));
    }

}
