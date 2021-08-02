package org.forzm.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void saveUserProfile(MultipartFile multipartFile);
    void saveForumImage(MultipartFile multipartFile, String title);
}
