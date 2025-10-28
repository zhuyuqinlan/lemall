package org.zhuyuqinlan.lemall.common.file.service.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.FileInfoExistDTO;
import org.zhuyuqinlan.lemall.common.file.dto.ext.MultipartUploadInfo;

@Service
@Slf4j // TODO 完成minio Service
public class MinioFileService {

    public String accessCode(String token, boolean isPublic) {
        return null;
    }

    public FileInfoExistDTO uploadUrl(String token, String accessCode, String md5, boolean isPublic) {
        return null;
    }

    public FileInfoDTO complete(String token, String uploadId, boolean isPublic) {
        return null;
    }

    public MultipartUploadInfo multipartUploadInfoResult(String token, String accessCode, String md5, boolean isPublic) {
        return null;
    }

    public FileInfoDTO completeMultipart(String token, String uploadId, boolean isPublic) {
        return null;
    }
}
