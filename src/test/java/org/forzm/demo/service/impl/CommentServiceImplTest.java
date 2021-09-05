package org.forzm.demo.service.impl;

import org.forzm.demo.dto.CommentRequestDto;
import org.forzm.demo.dto.CommentResponseDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.model.*;
import org.forzm.demo.repository.CommentRepository;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private AuthService authService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CommentServiceImpl commentService;
    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    @Test
    void shouldAddComment() {
        User currentUser = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        Forum forum = new Forum(1L, "TEST_FORUM", "FORUM DESC", ForumGameType.FPS, null, currentUser, emptyList(), Instant.now());
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, PostType.DISCUSSION, null,  currentUser, forum, null, null);
        CommentRequestDto commentRequestDto = new CommentRequestDto("TEST CONTENT", "TEST_TITLE", 1L);
        Comment comment = new Comment(1L, "TEST CONTENT", null, post, currentUser);

        when(postRepository.findPostByTitleAndId(commentRequestDto.getPostTitle(), commentRequestDto.getPostId())).thenReturn(Optional.of(post));
        when(modelMapper.map(commentRequestDto, Comment.class)).thenReturn(comment);
        when(authService.getCurrentUser()).thenReturn(currentUser);

        commentService.addComment(commentRequestDto);

        verify(postRepository, times(1)).findPostByTitleAndId(anyString(), anyLong());
        verify(commentRepository, times(1)).save(commentArgumentCaptor.capture());
        verify(modelMapper, times(1)).map(commentRequestDto, Comment.class);
        verify(authService, times(1)).getCurrentUser();
        verifyNoMoreInteractions(postRepository);
        verifyNoMoreInteractions(commentRepository);

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isNotNull();
        assertThat(capturedComment).isInstanceOf(Comment.class);
        assertThat(capturedComment).isEqualTo(comment);
    }

    @Test
    void getAllPostComments() {
        Comment comment = new Comment(1L, "TEST CONTENT", null, null, null);
        CommentResponseDto commentResponseDto = new CommentResponseDto("TEST CONTENT", null, null, null, null, null, null, null, null);
        Post post = new Post(1L, "TEST_TITLE", "TEST CONTENT", null, PostType.DISCUSSION, null, null, null, null, null);
        List<Comment> comments = Collections.singletonList(comment);

        when(postRepository.findPostByTitleAndId(anyString(), anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPost(any(Post.class))).thenReturn(comments);
        when(modelMapper.map(comment, CommentResponseDto.class)).thenReturn(commentResponseDto);

        List<CommentResponseDto> actualAllPostComments = commentService.getAllPostComments("TEST", 1L);

        verify(postRepository, times(1)).findPostByTitleAndId(anyString(), anyLong());
        verify(commentRepository, times(1)).findAllByPost(any(Post.class));
        verify(modelMapper, times(1)).map(comment, CommentResponseDto.class);
        verifyNoMoreInteractions(postRepository);
        verifyNoMoreInteractions(commentRepository);

        assertThat(actualAllPostComments).isNotNull();
        assertThat(actualAllPostComments.size()).isEqualTo(1);
        assertThat(actualAllPostComments.get(0)).isInstanceOf(CommentResponseDto.class);
        assertThat(actualAllPostComments.get(0)).isEqualTo(commentResponseDto);
        assertThat(commentResponseDto).isIn(actualAllPostComments);
    }

    @Test
    void getUserCommentCount() {
        Long userCommentCount = 1L;

        when(commentRepository.countAllByUserUsername(anyString())).thenReturn(userCommentCount);

        Long actualUserCommentCount = commentService.getUserCommentCount("username");

        verify(commentRepository, times(1)).countAllByUserUsername(anyString());
        verifyNoMoreInteractions(commentRepository);

        assertThat(actualUserCommentCount).isNotNull();
        assertThat(actualUserCommentCount).isInstanceOf(Long.class);
        assertThat(actualUserCommentCount).isEqualTo(1L);
    }

    @Test
    void getAllUserCommentsByUsername() {
        Comment comment = new Comment(1L, "TEST CONTENT", null, null, null);
        CommentResponseDto commentResponseDto = new CommentResponseDto("TEST CONTENT", null, null, null, null, null, null, null, null);
        List<Comment> comments = Collections.singletonList(comment);

        when(commentRepository.findAllByUserUsername(anyString())).thenReturn(comments);
        when(modelMapper.map(comment, CommentResponseDto.class)).thenReturn(commentResponseDto);

        List<CommentResponseDto> actualUserComments = commentService.getAllUserCommentsByUsername("username");

        verify(commentRepository, times(1)).findAllByUserUsername(anyString());
        verify(modelMapper, times(1)).map(comment, CommentResponseDto.class);
        verifyNoMoreInteractions(commentRepository);

        assertThat(actualUserComments).isNotNull();
        assertThat(actualUserComments.size()).isEqualTo(1);
        assertThat(actualUserComments.get(0)).isInstanceOf(CommentResponseDto.class);
        assertThat(actualUserComments.get(0)).isEqualTo(commentResponseDto);
        assertThat(commentResponseDto).isIn(actualUserComments);
    }

    @Test
    void getUserFiveRecentComments() {
        Comment comment = new Comment(1L, "TEST CONTENT", null, null, null);
        CommentResponseDto commentResponseDto = new CommentResponseDto("TEST CONTENT", null, null, null, null, null, null, null, null);
        List<Comment> comments = Collections.singletonList(comment);

        when(commentRepository.findTop5ByUserUsernameOrderByDateRepliedAsc(anyString())).thenReturn(comments);
        when(modelMapper.map(comment, CommentResponseDto.class)).thenReturn(commentResponseDto);

        List<CommentResponseDto> actualUserComments = commentService.getUserFiveRecentComments("username");

        verify(commentRepository, times(1)).findTop5ByUserUsernameOrderByDateRepliedAsc(anyString());
        verify(modelMapper, times(1)).map(comment, CommentResponseDto.class);
        verifyNoMoreInteractions(commentRepository);

        assertThat(actualUserComments).isNotNull();
        assertThat(actualUserComments.size()).isEqualTo(1);
        assertThat(actualUserComments.get(0)).isInstanceOf(CommentResponseDto.class);
        assertThat(actualUserComments.get(0)).isEqualTo(commentResponseDto);
        assertThat(commentResponseDto).isIn(actualUserComments);
    }

    @Test
    void mapToComment() {
        Comment comment = new Comment(1L, "TEST CONTENT", null, null, null);
        CommentRequestDto commentRequestDto = new CommentRequestDto("TEST CONTENT", "TEST_TITLE", 1L);

        when(modelMapper.map(commentRequestDto, Comment.class)).thenReturn(comment);

        Comment actualComment = commentService.mapToComment(commentRequestDto);

        verify(modelMapper, times(1)).map(commentRequestDto, Comment.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualComment).isNotNull();
        assertThat(actualComment).isInstanceOf(Comment.class);
        assertThat(actualComment).isEqualTo(comment);
    }

    @Test
    void mapToCommentResponseDto() {
        Comment comment = new Comment(1L, "TEST CONTENT", null, null, null);
        CommentResponseDto commentResponseDto = new CommentResponseDto("TEST CONTENT", null, null, null, null, null, null, null, null);

        when(modelMapper.map(comment, CommentResponseDto.class)).thenReturn(commentResponseDto);

        CommentResponseDto actualCommentResponseDto = commentService.mapToCommentResponseDto(comment);

        verify(modelMapper, times(1)).map(comment, CommentResponseDto.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualCommentResponseDto).isNotNull();
        assertThat(actualCommentResponseDto).isInstanceOf(CommentResponseDto.class);
        assertThat(actualCommentResponseDto).isEqualTo(commentResponseDto);
    }
}