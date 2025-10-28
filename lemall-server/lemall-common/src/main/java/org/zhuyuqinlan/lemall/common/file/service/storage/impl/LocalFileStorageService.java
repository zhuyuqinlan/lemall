package org.zhuyuqinlan.lemall.common.file.service.storage.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.zhuyuqinlan.lemall.common.entity.FsFileStorage;
import org.zhuyuqinlan.lemall.common.file.constant.FileStorageConstant;
import org.zhuyuqinlan.lemall.common.file.dto.FileInfoDTO;
import org.zhuyuqinlan.lemall.common.file.service.storage.FileStorageService;
import org.zhuyuqinlan.lemall.common.mapper.FsFileStorageMapper;

import java.io.*;
import java.nio.file.*;

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
    @SneakyThrows
    public FileInfoDTO uploadFile(String fileKey, InputStream inputStream, long size, String contentType, String md5) {
        Path filePath = Paths.get(basePath, fileKey);
        Files.createDirectories(filePath.getParent());
        FileCopyUtils.copy(inputStream, Files.newOutputStream(filePath));
        FsFileStorage fsFileStorage = new FsFileStorage();
        fsFileStorage.setStorageType(FileStorageConstant.LOCAL_TYPE);
        fsFileStorage.setContentType(contentType);
        fsFileStorage.setSize(size);
        fsFileStorage.setUri(fileKey);
        fsFileStorage.setFileKey(fileKey);
        fsFileStorage.setMd5(md5);
        fileStorageMapper.insert(fsFileStorage);
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(fsFileStorage, fileInfoDTO);
        fileInfoDTO.setUrl(getFileUrl(fileKey));
        return fileInfoDTO;
    }

    @Override
    @SneakyThrows
    public void deleteFile(String fileKey) {
        Files.deleteIfExists(Paths.get(basePath, fileKey));
        fileStorageMapper.delete(Wrappers.<FsFileStorage>lambdaQuery().eq(FsFileStorage::getFileKey, fileKey));
    }

    @Override
    @SneakyThrows
    public InputStream downloadFile(String fileKey) {
        return Files.newInputStream(Paths.get(basePath, fileKey));
    }

    @Override
    public String getFileUrl(String fileKey) {
        return accessUrlPrefix + "/" + fileKey;
    }
}

