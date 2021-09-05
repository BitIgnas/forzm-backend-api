package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.NoUserFoundException;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.FileService;
import org.forzm.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final FileService fileService;

    @Value("${cloud.aws.bucket.user}")
    private String userBucket;

    @Value("${cloud.aws.bucket.forum}")
    private String forumBucket;

    @Value("${cloud.aws.bucket.post}")
    private String postBucket;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg");

    @Override
    @Transactional
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
    @Transactional
    public void saveForumImage(MultipartFile multipartFile, String title) {
        String forumName = decodeUrl(title);

        Forum forum = forumRepository.getForumByName(forumName)
                .orElseThrow(() -> new ForumException("No forum was found"));

        if (CONTENT_TYPES.contains(multipartFile.getContentType()) && multipartFile.getSize() < 10485760) {
            String imgUrl = fileService.uploadFile(multipartFile, forumBucket);
            forum.setImageUrl(imgUrl);
            forumRepository.save(forum);
        } else if (!CONTENT_TYPES.contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (multipartFile.getSize() < 10485760) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE);
        }
    }

    @Override
    @Transactional
    public void savePostImage(MultipartFile multipartFile, String postTitle, Long postId) {
        String userPostTitle = decodeUrl(postTitle);

        Post post = postRepository.findPostByTitleAndId(userPostTitle, postId)
                .orElseThrow(() -> new PostException("No post was found"));

            String imgUrl = fileService.uploadFile(multipartFile, postBucket);
            post.setPostImageUrl(imgUrl);
            postRepository.save(post);

    }

    @Override
    @Transactional
    public void updateUserProfile(MultipartFile multipartFile, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoUserFoundException("No user was found"));

        if (CONTENT_TYPES.contains(multipartFile.getContentType()) && user.getProfileImageUrl() != null &&
                multipartFile.getSize() < 10485760) {
            fileService.deleteFile(user.getProfileImageUrl(), userBucket);
            String imgUrl = fileService.uploadFile(multipartFile, userBucket);
            user.setProfileImageUrl(imgUrl);
        } else if (user.getProfileImageUrl() == null) {
            String imgUrl = fileService.uploadFile(multipartFile, userBucket);
            user.setProfileImageUrl(imgUrl);
        } else if (!CONTENT_TYPES.contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (multipartFile.getSize() > 10485760) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE);
        }

        userRepository.save(user);
    }

    @Override
    public String decodeUrl(String title) {
        try {
            return URLDecoder.decode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
        }

        return null;
    }
}
