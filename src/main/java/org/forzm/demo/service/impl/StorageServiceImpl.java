package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.FileService;
import org.forzm.demo.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final AuthService authService;
    private final FileService fileService;

    private final static String USER_BUCKET = "forzm-user-profile";
    private final static String FORUM_BUCKET = "forzm-forum-img";


    @Override
    public void saveUserProfile(MultipartFile multipartFile) {
        User user = authService.getCurrentUser();

        String imgUrl = fileService.uploadFile(multipartFile, USER_BUCKET);
        user.setProfileImageUrl(imgUrl);
        userRepository.save(user);
    }

    @Override
    public void saveForumImage(MultipartFile multipartFile, String title) {
        Forum forum = forumRepository.getForumByName(title)
                .orElseThrow(() -> new ForumException("No forum was found"));

        String imgUrl = fileService.uploadFile(multipartFile, FORUM_BUCKET);
        forum.setImageUrl(imgUrl);
        forumRepository.save(forum);
    }
}
