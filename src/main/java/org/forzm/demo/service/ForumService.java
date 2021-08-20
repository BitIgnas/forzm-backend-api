package org.forzm.demo.service;

import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.model.Forum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForumService {
    ForumResponseDto createForum(ForumRequestDto forumRequestDto);
    List<ForumResponseDto> findForumByNameIgnoreCase(String name);
    void deleteForum(ForumRequestDto ForumRequestDto);
    void checkIfForumExist(String forumName);
    List<ForumResponseDto> findUserForumsByUsername(String username);
    ForumResponseDto findForumByName(String name);
    ForumResponseDto mapToForumResponseDto(Forum forum);
    Forum mapToForum(ForumRequestDto forumRequestDto);
    List<ForumResponseDto> getAllForums();
}
