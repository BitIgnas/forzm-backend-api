package org.forzm.demo.service.impl;

import org.checkerframework.checker.nullness.Opt;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.PostRequestDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.model.*;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private ForumRepository forumRepository;
    @Mock
    private AuthService authService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PostServiceImpl postService;
    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @Test
    void shouldAddPostToForum() {
        User currentUser = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        PostRequestDto postRequestDto = new PostRequestDto("TEST_TITLE", "TEST CONTENT", null, null, "TEST_TITLE");
        Forum forum = new Forum(1L, "TEST_FORUM", "FORUM DESC", ForumGameType.FPS, null, currentUser, emptyList(), Instant.now());
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, forum, null, null);

        when(forumRepository.getForumByName(postRequestDto.getForumName())).thenReturn(Optional.of(forum));
        when(modelMapper.map(postRequestDto, Post.class)).thenReturn(post);
        when(authService.getCurrentUser()).thenReturn(currentUser);

        postService.addPost(postRequestDto);

        verify(postRepository, times(1)).save(postArgumentCaptor.capture());
        verify(forumRepository, times(1)).getForumByName(anyString());
        verify(authService, times(1)).getCurrentUser();
        verify(modelMapper, times(1)).map(postRequestDto, Post.class);
        verifyNoMoreInteractions(postRepository);
        verifyNoMoreInteractions(authService);

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isNotNull();
        assertThat(capturedPost).isInstanceOf(Post.class);
        assertThat(capturedPost).isEqualTo(post);
        assertThat(capturedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(capturedPost.getContent()).isEqualTo(post.getContent());
        assertThat(capturedPost.getForum().getName()).isEqualTo(post.getForum().getName());
    }

    @Test
    void findAllPostsByForumNameAndByPostType() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostResponseDto postResponseDto = new PostResponseDto("TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null, null, null, null);
        List<Post> posts = Collections.singletonList(post);

        when(postRepository.findAllByForumNameAndPostType(anyString(), any(PostType.class))).thenReturn(posts);
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(postResponseDto);

        List<PostResponseDto> actualPosts = postService.findAllPostsByForumNameAndByPostType("TEST", PostType.UPDATES.name());

        verify(postRepository, times(1)).findAllByForumNameAndPostType(anyString(), any(PostType.class));
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
        verifyNoMoreInteractions(postRepository);

        assertThat(actualPosts).isNotNull();
        assertThat(actualPosts.size()).isEqualTo(1);
        assertThat(actualPosts.get(0)).isInstanceOf(PostResponseDto.class);
        assertThat(actualPosts.get(0)).isEqualTo(postResponseDto);
        assertThat(postResponseDto).isIn(actualPosts);

    }

    @Test
    void findByPostTitleAndId() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostResponseDto postResponseDto = new PostResponseDto("TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null, null, null, null);

        when(postRepository.findPostByTitleAndId(anyString(), anyLong())).thenReturn(Optional.of(post));
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(postResponseDto);

        PostResponseDto actualPostResponseDto = postService.findByPostTitleAndId("TEST", 23L);

        verify(postRepository, times(1)).findPostByTitleAndId(anyString(), anyLong());
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
        verifyNoMoreInteractions(postRepository);

        assertThat(actualPostResponseDto).isNotNull();
        assertThat(actualPostResponseDto).isInstanceOf(PostResponseDto.class);
        assertThat(actualPostResponseDto).isEqualTo(postResponseDto);
    }

    @Test
    void getAllUserPostsByUsername() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostResponseDto postResponseDto = new PostResponseDto("TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null, null, null, null);
        List<Post> posts = Collections.singletonList(post);

        when(postRepository.findAllByUserUsername(anyString())).thenReturn(posts);
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(postResponseDto);

        List<PostResponseDto> actualUserPosts = postService.getAllUserPostsByUsername("TEST");

        verify(postRepository, times(1)).findAllByUserUsername(anyString());
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
        verifyNoMoreInteractions(postRepository);

        assertThat(actualUserPosts).isNotNull();
        assertThat(actualUserPosts.size()).isEqualTo(1);
        assertThat(actualUserPosts.get(0)).isInstanceOf(PostResponseDto.class);
        assertThat(actualUserPosts.get(0)).isEqualTo(postResponseDto);
        assertThat(postResponseDto).isIn(actualUserPosts);
    }

    @Test
    void getUserFiveRecentCreatedPosts() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostResponseDto postResponseDto = new PostResponseDto("TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null, null, null, null);
        List<Post> posts = Collections.singletonList(post);

        when(postRepository.findTop5ByUserUsernameOrderByCreatedDesc(anyString())).thenReturn(posts);
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(postResponseDto);

        List<PostResponseDto> actualUserPosts = postService.getUserFiveRecentCreatedPosts("TEST");

        verify(postRepository, times(1)).findTop5ByUserUsernameOrderByCreatedDesc(anyString());
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
        verifyNoMoreInteractions(postRepository);

        assertThat(actualUserPosts).isNotNull();
        assertThat(actualUserPosts.size()).isEqualTo(1);
        assertThat(actualUserPosts.get(0)).isInstanceOf(PostResponseDto.class);
        assertThat(actualUserPosts.get(0)).isEqualTo(postResponseDto);
        assertThat(postResponseDto).isIn(actualUserPosts);
    }

    @Test
    void countAllForumPosts() {
        Long userPostCount = 1L;

        when(postRepository.countAllByForumName(anyString())).thenReturn(userPostCount);

        Long actualUserPostCount = postService.countAllForumPosts("TEST");

        verify(postRepository, times(1)).countAllByForumName(anyString());
        verifyNoMoreInteractions(forumRepository);

        assertThat(actualUserPostCount).isNotNull();
        assertThat(actualUserPostCount).isExactlyInstanceOf(Long.class);
        assertThat(actualUserPostCount).isEqualTo(userPostCount);
    }

    @Test
    void countAllUserPosts() {
        Long userPostCount = 1L;

        when(postRepository.countAllByUserUsername(anyString())).thenReturn(userPostCount);

        Long actualUserPostCount = postService.countAllUserPosts("TEST");

        verify(postRepository, times(1)).countAllByUserUsername(anyString());
        verifyNoMoreInteractions(forumRepository);

        assertThat(actualUserPostCount).isNotNull();
        assertThat(actualUserPostCount).isExactlyInstanceOf(Long.class);
        assertThat(actualUserPostCount).isEqualTo(userPostCount);
    }

    @Test
    void mapToPost() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostRequestDto postRequestDto = new PostRequestDto("TEST_TITLE", "TEST CONTENT", null, null, "TEST_TITLE");

        when(modelMapper.map(postRequestDto, Post.class)).thenReturn(post);

        Post actualPost = postService.mapToPost(postRequestDto);

        verify(modelMapper, times(1)).map(postRequestDto, Post.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualPost).isNotNull();
        assertThat(actualPost).isInstanceOf(Post.class);
        assertThat(actualPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(actualPost.getContent()).isEqualTo(post.getContent());
    }

    @Test
    void mapToPostResponseDto() {
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null);
        PostResponseDto postResponseDto = new PostResponseDto("TEST_TITLE", "TEST CONTENT", null, null, null, null, null, null, null, null, null);

        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(postResponseDto);

        PostResponseDto actualPostResponseDto = postService.mapToPostResponseDto(post);

        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualPostResponseDto).isNotNull();
        assertThat(actualPostResponseDto).isInstanceOf(PostResponseDto.class);
        assertThat(actualPostResponseDto.getTitle()).isEqualTo(postResponseDto.getTitle());
        assertThat(actualPostResponseDto.getContent()).isEqualTo(postResponseDto.getContent());
    }
}