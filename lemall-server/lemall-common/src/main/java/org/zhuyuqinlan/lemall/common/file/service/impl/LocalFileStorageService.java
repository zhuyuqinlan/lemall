package org.zhuyuqinlan.lemall.common.file.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.service.FileStorageService;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${localFile.base-path}")
    private String basePath; // 本地文件根目录

    @Value("${localFile.access-url-prefix}")
    private String accessUrlPrefix; // 网络访问前缀

    private final FsFileStorageMapper fileStorageMapper;

    public LocalFileStorageService(FsFileStorageMapper fileStorageMapper) {
        this.fileStorageMapper = fileStorageMapper;
    }

    @Override
    public Map<String,String> uploadFile(String objectName, InputStream inputStream, long size, String contentType) {
        try {
            Path filePath = Paths.get(basePath, objectName);
            Files.createDirectories(filePath.getParent());
            FileCopyUtils.copy(inputStream, Files.newOutputStream(filePath));
            FsFileStorage fsFileStorage = new FsFileStorage();
            fsFileStorage.setStorageType(FileStorageConstant.LOCAL_TYPE);
            fsFileStorage.setContentType(contentType);
            fsFileStorage.setSize(size);
            fsFileStorage.setUri(objectName);
            fsFileStorage.setOriginalName(objectName);
            fileStorageMapper.insert(fsFileStorage);
            Long id = fsFileStorage.getId();
            Map<String,String> map = new HashMap<>();
            map.put("id",id.toString());
            map.put("url",getFileUrl(objectName));
            return map;
        } catch (IOException e) {
            throw new RuntimeException("本地文件上传失败", e);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            Files.deleteIfExists(Paths.get(basePath, objectName));
            fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getOriginalName, objectName));
        } catch (IOException e) {
            throw new RuntimeException("删除本地文件失败", e);
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {
        try {
            return Files.newInputStream(Paths.get(basePath, objectName));
        } catch (IOException e) {
            throw new RuntimeException("本地文件下载失败", e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return accessUrlPrefix + "/" + objectName;
    }
}

