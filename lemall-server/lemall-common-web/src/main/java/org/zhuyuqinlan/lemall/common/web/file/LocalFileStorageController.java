package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
import org.zhuyuqinlan.lemall.common.response.Result;
import org.zhuyuqinlan.lemall.common.file.service.biz.LocalFileService;

@RestController
@RequestMapping("${lemall.server.prefix.common}/file/local")
@RequiredArgsConstructor
@Tag(name = "本地文件")
@Slf4j
public class LocalFileStorageController {

    private final LocalFileService localFileService;

    @Value("${sa-token.token-name}")
    private String SA_TOKEN_NAME;

    @GetMapping("/access-code")
    @Operation(summary = "获取上传accessCode")
    public Result<String> getAccessCode(HttpServletRequest request) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(localFileService.getAccessCode(token));
    }

    @PostMapping("/check")
    @Operation(summary = "检查文件MD5")
    public Result<FileInfoExistDTO> check(HttpServletRequest request,
                                          @RequestParam String accessCode,
                                          @RequestParam String md5) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(localFileService.checkFile(token, md5, accessCode));
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<FileInfoDTO> upload(@RequestParam MultipartFile file,
                                      @RequestParam String uploadId,
                                      HttpServletRequest request) {
        String token = request.getHeader(SA_TOKEN_NAME);
        return Result.success(localFileService.uploadFile(token, uploadId, file));
    }
}
