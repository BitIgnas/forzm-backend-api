package org.forzm.demo.service;

import org.forzm.demo.dto.PostRequestDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.model.Post;

import java.util.List;

public interface PostService {
    PostRequestDto addPost(PostRequestDto postRequestDto);
    List<PostResponseDto> findAllPostsByForumNameAndByPostType(String forumName, String forumType);
    PostRequestDto findPostByTitle(String name);
    void deletePost(PostRequestDto postRequestDto);
    Long countAllForumPosts(String forumName);
    Post mapToPost(PostRequestDto postRequestDto);
    PostRequestDto mapToPostRequestDto(Post post);
    PostResponseDto mapToPostResponseDto(Post post);
}
