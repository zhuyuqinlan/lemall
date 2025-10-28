package org.zhuyuqinlan.lemall.common.web.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhuyuqinlan.lemall.common.file.service.biz.LocalFileService;
import org.zhuyuqinlan.lemall.common.response.Result;

@Profile("dev")  // 只在 dev 环境生效
@Slf4j
@RestController
@Tag(name = "文件操作(开发环境专用)", description = "LocalFileStorageController")
public class DevFileController {

    private final LocalFileService localFileService;

    public DevFileController(LocalFileService localFileService) {
        this.localFileService = localFileService;
    }


    /**
     * 浏览器访问或下载文件（本地存储）
     */
    @GetMapping("${localFile.local-download-prefix}/**")
    public void serveFile(HttpServletRequest request, HttpServletResponse response) {
        localFileService.serveFile(request.getRequestURI(), response);
    }

    @Operation(summary = "删除文件（local）")
    @PostMapping("${lemall.server.prefix.common}/file/dev/local/delete")
    public Result<?> deleteFileLocal(@RequestParam("objectName") String objectName) {
        localFileService.deleteFile(objectName);
        return Result.success();
    }

}
