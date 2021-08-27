package org.forzm.demo.service.impl;

import org.checkerframework.checker.nullness.Opt;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.PostResponseDto;
import org.forzm.demo.exception.ForumExistsException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.ForumService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForumServiceImplTest {

    @Mock
    private ForumRepository forumRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AuthService authService;
    @InjectMocks
    private ForumServiceImpl forumService;
    @Captor
    private ArgumentCaptor<Forum> forumArgumentCaptor;

    @Test
    void shouldCreateForum() {
        User currentUser = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        Forum forum = new Forum(1L, "TEST_FORUM", "FORUM DESC", ForumGameType.FPS, null, currentUser, emptyList(), Instant.now());
        ForumRequestDto forumRequestDto = new ForumRequestDto("TEST_FORUM", ForumGameType.FPS, "FORUM DESC");

        when(modelMapper.map(forumRequestDto, Forum.class)).thenReturn(forum);
        when(authService.getCurrentUser()).thenReturn(currentUser);

        forumService.createForum(forumRequestDto);

        verify(forumRepository, times(1)).save(forumArgumentCaptor.capture());
        verify(modelMapper, times(1)).map(forumRequestDto, Forum.class);
        verify(authService, times(1)).getCurrentUser();

        Forum capturedForum = forumArgumentCaptor.getValue();

        assertThat(capturedForum).isNotNull();
        assertThat(capturedForum).isExactlyInstanceOf(Forum.class);
        assertThat(capturedForum.getName()).isEqualTo(forumRequestDto.getName());
        assertThat(capturedForum.getDescription()).isEqualTo(forumRequestDto.getDescription());
        assertThat(capturedForum.getForumGameType()).isEqualTo(forumRequestDto.getForumGameType());
    }

    @Test
    void shouldThrowForumExistsExceptionWhenCreatingForum() {
        Forum forum = new Forum(1L, "TEST_FORUM", "FORUM DESC", ForumGameType.FPS, null, null, emptyList(), Instant.now());
        ForumRequestDto forumRequestDto = new ForumRequestDto("TEST_FORUM", ForumGameType.FPS, "FORUM DESC");

        when(forumRepository.getForumByName(anyString())).thenReturn(Optional.of(forum));

        Throwable thrown = catchThrowable(() -> {
            forumService.createForum(forumRequestDto);
        });

        assertThat(thrown).isInstanceOf(ForumExistsException.class).hasMessage("Forum already exists");
    }

    @Test
    void findForumByNameIgnoreCase() {
        Forum forum1 = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        Forum forum2 = new Forum(2L, "FORUM_TEST2", "FORUM DESC2", ForumGameType.FPS, null, null, null, null);
        ForumResponseDto forumResponseDto1 = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);
        ForumResponseDto forumResponseDto2 = new ForumResponseDto("FORUM_TEST2", "FORUM DESC2", ForumGameType.FPS, null, null);
        List<Forum> forums = Arrays.asList(forum1, forum2);

        doReturn(forums).when(forumRepository).getAllByNameContainingIgnoreCase(anyString());
        when(modelMapper.map(forum1, ForumResponseDto.class)).thenReturn(forumResponseDto1);
        when(modelMapper.map(forum2, ForumResponseDto.class)).thenReturn(forumResponseDto2);

        List<ForumResponseDto> actualForums = forumService.findForumByNameIgnoreCase("FORUM");

        verify(forumRepository, times(1)).getAllByNameContainingIgnoreCase("FORUM");
        verify(modelMapper, times(1)).map(forum1, ForumResponseDto.class);
        verifyNoMoreInteractions(forumRepository);

        assertThat(actualForums).isNotNull();
        assertThat(actualForums.size()).isEqualTo(2);
        assertThat(actualForums.get(0)).isInstanceOf(ForumResponseDto.class);
        assertThat(actualForums.get(0).getName()).isEqualTo(forum1.getName());
        assertThat(actualForums.get(0).getDescription()).isEqualTo(forum1.getDescription());
        assertThat(actualForums.get(0).getForumGameType()).isEqualTo(forum1.getForumGameType());
        assertThat(actualForums.get(1)).isInstanceOf(ForumResponseDto.class);
        assertThat(actualForums.get(1).getName()).isEqualTo(forum2.getName());
        assertThat(actualForums.get(1).getDescription()).isEqualTo(forum2.getDescription());
        assertThat(actualForums.get(1).getForumGameType()).isEqualTo(forum2.getForumGameType());
        assertThat(forumResponseDto1).isIn(actualForums);
        assertThat(forumResponseDto2).isIn(actualForums);
    }

    @Test
    void getAllForums() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_NAME", "FORUM DESC", ForumGameType.FPS, null, null);
        List<Forum> forums = Collections.singletonList(forum);

        when(forumRepository.getAllByOrderByPostsAsc()).thenReturn(forums);
        when(modelMapper.map(forum, ForumResponseDto.class)).thenReturn(forumResponseDto);

        List<ForumResponseDto> actualForums = forumService.getAllForums();

        assertThat(actualForums.size()).isEqualTo(1);
        assertThat(actualForums.get(0)).isExactlyInstanceOf(ForumResponseDto.class);
        assertThat(actualForums.get(0).getName()).isEqualTo(forumResponseDto.getName());
        assertThat(actualForums.get(0).getForumGameType()).isEqualTo(forumResponseDto.getForumGameType());
        assertThat(actualForums.get(0).getDescription()).isEqualTo(forumResponseDto.getDescription());
        assertThat(forumResponseDto).isIn(actualForums);
    }

    @Test
    void deleteForum() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);

        when(forumRepository.getForumByName(anyString())).thenReturn(Optional.of(forum));
        doNothing().when(forumRepository).delete(any(Forum.class));

        forumService.deleteForum("test");

        verify(forumRepository, times(1)).delete(forumArgumentCaptor.capture());
        verify(forumRepository, times(1)).getForumByName(anyString());
        verifyNoMoreInteractions(forumRepository);

        Forum capturedForum = forumArgumentCaptor.getValue();

        assertThat(capturedForum).isNotNull();
        assertThat(capturedForum).isExactlyInstanceOf(Forum.class);
        assertThat(capturedForum).isEqualTo(forum);

    }

    @Test
    void findUserForumsByUsername() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);
        List<Forum> forums = Collections.singletonList(forum);

        when(forumRepository.findForumsByUser_Username(anyString())).thenReturn(forums);
        when(modelMapper.map(forum, ForumResponseDto.class)).thenReturn(forumResponseDto);

        List<ForumResponseDto> actualUserForums = forumService.findUserForumsByUsername("username");

        verify(forumRepository, times(1)).findForumsByUser_Username(anyString());
        verify(modelMapper, times(1)).map(forum, ForumResponseDto.class);
        verifyNoMoreInteractions(forumRepository);

        assertThat(actualUserForums).isNotNull();
        assertThat(actualUserForums.size()).isEqualTo(1);
        assertThat(actualUserForums.get(0)).isInstanceOf(ForumResponseDto.class);
        assertThat(actualUserForums.get(0).getName()).isEqualTo(forumResponseDto.getName());
        assertThat(actualUserForums.get(0).getDescription()).isEqualTo(forumResponseDto.getDescription());
        assertThat(actualUserForums.get(0).getForumGameType()).isEqualTo(forumResponseDto.getForumGameType());
        assertThat(forum).isIn(forums);
    }

    @Test
    void findForumByName() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);

        when(forumRepository.getForumByName("FORUMS")).thenReturn(Optional.of(forum));
        when(modelMapper.map(forum, ForumResponseDto.class)).thenReturn(forumResponseDto);

        ForumResponseDto allForums = forumService.findForumByName("FORUMS");

        verify(forumRepository, times(1)).getForumByName(anyString());
        verify(modelMapper, times(1)).map(forum, ForumResponseDto.class);
        verifyNoMoreInteractions(forumRepository);

        assertThat(allForums).isNotNull();
        assertThat(allForums).isEqualTo(forumResponseDto);
    }

    @Test
    void mapToForumResponseDto() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);

        when(modelMapper.map(forum, ForumResponseDto.class)).thenReturn(forumResponseDto);

        ForumResponseDto actualForumResponseDto = forumService.mapToForumResponseDto(forum);

        verify(modelMapper, times(1)).map(forum, ForumResponseDto.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualForumResponseDto).isNotNull();
        assertThat(actualForumResponseDto).isInstanceOf(ForumResponseDto.class);
        assertThat(actualForumResponseDto.getName()).isEqualTo(forumResponseDto.getName());
        assertThat(actualForumResponseDto.getDescription()).isEqualTo(forumResponseDto.getDescription());
        assertThat(actualForumResponseDto.getForumGameType()).isEqualTo(forumResponseDto.getForumGameType());
    }

    @Test
    void mapToForum() {
        Forum forum = new Forum(1L, "FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        ForumRequestDto forumRequestDto = new ForumRequestDto("TEST_FORUM", ForumGameType.FPS, "FORUM DESC");

        when(modelMapper.map(forumRequestDto, Forum.class)).thenReturn(forum);

        Forum actualForum = forumService.mapToForum(forumRequestDto);

        verify(modelMapper, times(1)).map(forumRequestDto, Forum.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualForum).isNotNull();
        assertThat(actualForum).isInstanceOf(Forum.class);
        assertThat(actualForum.getName()).isEqualTo(forum.getName());
        assertThat(actualForum.getDescription()).isEqualTo(forum.getDescription());
        assertThat(actualForum.getForumGameType()).isEqualTo(forum.getForumGameType());
    }
}