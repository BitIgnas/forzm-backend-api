package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.dto.PostRequestDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.forzm.demo.model.PostType;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ForumRepository forumRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto) {
        Forum forum = forumRepository.getForumByName(postRequestDto.getForumName())
                .orElseThrow(() -> new PostException("No forum was found"));
        Post post = mapToPost(postRequestDto);
        post.setForum(forum);
        post.setUser(authService.getCurrentUser());
        post.setCreated(Instant.now());

        return mapToPostResponseDto(postRepository.save(post));
    }

    @Override
    public List<PostResponseDto> findAllPostsByForumNameAndByPostType(String forumName, String postType) {
        return postRepository.findAllByForumNameAndPostType(forumName, PostType.valueOf(postType.toUpperCase())).stream()
                .map(this::mapToPostResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostResponseDto findByPostTitleAndId(String name, Long id) {
        Post post = postRepository.findPostByTitleAndId(name, id)
                .orElseThrow(() -> new PostException("No posts where found"));
        return mapToPostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePost(PostRequestDto postRequestDto) {
        postRepository.delete(mapToPost(postRequestDto));
    }

    @Override
    public List<PostResponseDto> getAllUserPostsByUsername(String username) {
            return postRepository.findAllByUserUsername(username).stream()
                    .map(this::mapToPostResponseDto)
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long countAllForumPosts(String forumName) {
        return postRepository.countAllByForumName(forumName);
    }

    @Override
    public Long countAllUserPosts(String username) {
        return postRepository.countAllByUserUsername(username);
    }

    @Override
    public Post mapToPost(PostRequestDto postRequestDto) {
        return modelMapper.map(postRequestDto, Post.class);
    }

    @Override
    public PostResponseDto mapToPostResponseDto(Post post) {
        return modelMapper.map(post, PostResponseDto.class);
    }
}

