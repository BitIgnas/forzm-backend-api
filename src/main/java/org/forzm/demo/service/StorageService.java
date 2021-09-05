package org.forzm.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface StorageService {
    void saveUserProfile(MultipartFile multipartFile, String username);
    void saveForumImage(MultipartFile multipartFile, String title);
    void savePostImage(MultipartFile multipartFile, String postTitle, Long postId);
    void updateUserProfile(MultipartFile multipartFile, String username);
    String decodeUrl(String title) throws UnsupportedEncodingException;
}
