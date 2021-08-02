package org.forzm.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile multipartFile, String bucket);
    void deleteFile(String url, String bucketName);
}

