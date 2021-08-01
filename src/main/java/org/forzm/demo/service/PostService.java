package org.forzm.demo.service;

import org.forzm.demo.dto.PostDto;
import org.forzm.demo.model.Post;

import java.util.List;

public interface PostService {
    PostDto addPost(PostDto postDto);
    List<PostDto> findAllPosts();
    PostDto findPostByTitle(String name);
    void deletePost(PostDto postDto);
    Post mapToPost(PostDto postDto);
    PostDto mapToPostDto(Post post);
}
