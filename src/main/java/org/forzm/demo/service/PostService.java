package org.forzm.demo.service;

import org.forzm.demo.dto.PostRequestDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.model.Post;

import java.util.List;

public interface PostService {
    PostResponseDto addPost(PostRequestDto postRequestDto);
    List<PostResponseDto> findAllPostsByForumNameAndByPostType(String forumName, String forumType);
    PostResponseDto findByPostTitleAndId(String name, Long id);
    List<PostResponseDto> getAllUserPostsByUsername(String username);
    List<PostResponseDto> getUserFiveRecentCreatedPosts(String username);
    Long countAllForumPosts(String forumName);
    Long countAllUserPosts(String username);
    Post mapToPost(PostRequestDto postRequestDto);
    PostResponseDto mapToPostResponseDto(Post post);
}
