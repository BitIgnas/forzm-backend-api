package org.forzm.demo.service;

import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForumService {
    ForumResponseDto createForum(ForumRequestDto forumRequestDto);
    List<ForumResponseDto> findForumByNameIgnoreCase(String name);
    void deleteForum(String name);
    List<ForumResponseDto> findForumsByGameType(ForumGameType gameType);
    boolean checkIfForumIsUsers(String forumName, String username);
    List<ForumResponseDto> findUserForumsByUsername(String username);
    ForumResponseDto findForumByName(String name);
    ForumResponseDto mapToForumResponseDto(Forum forum);
    Forum mapToForum(ForumRequestDto forumRequestDto);
    List<ForumResponseDto> getAllForums();
}
