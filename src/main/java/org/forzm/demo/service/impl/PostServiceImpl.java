package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.PostDto;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
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
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ForumRepository forumRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PostDto addPost(PostDto postDto) {
        Forum forum = forumRepository.getForumByName(postDto.getForumName())
                .orElseThrow(() -> new PostException("No forum was found"));
        Post post = mapToPost(postDto);
        post.setForum(forum);
        post.setUser(authService.getCurrentUser());
        post.setCreated(Instant.now());

        return mapToPostDto(postRepository.save(post));
    }

    @Override
    public List<PostDto> findAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDto findPostByTitle(String name) {
        Post post = postRepository.findByTitle(name).orElseThrow(() -> new PostException("No posts where found"));
        return mapToPostDto(post);
    }

    @Override
    @Transactional
    public void deletePost(PostDto postDto) {
        postRepository.delete(mapToPost(postDto));
    }

    @Override
    public Post mapToPost(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    @Override
    public PostDto mapToPostDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }
}

