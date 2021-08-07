package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.NoUserFoundException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.FileService;
import org.forzm.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final FileService fileService;

    @Value("${cloud.aws.bucket.user}")
    private String userBucket;

    @Value("${cloud.aws.bucket.forum}")
    private String forumBucket;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg");

    @Override
    public void saveUserProfile(MultipartFile multipartFile, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoUserFoundException("No user was found"));

        if (CONTENT_TYPES.contains(multipartFile.getContentType())) {
            String imgUrl = fileService.uploadFile(multipartFile, userBucket);
            user.setProfileImageUrl(imgUrl);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void saveForumImage(MultipartFile multipartFile, String title) {
        Forum forum = forumRepository.getForumByName(title)
                .orElseThrow(() -> new ForumException("No forum was found"));

        if (CONTENT_TYPES.contains(multipartFile.getContentType())) {
            String imgUrl = fileService.uploadFile(multipartFile, forumBucket);
            forum.setImageUrl(imgUrl);
            forumRepository.save(forum);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
