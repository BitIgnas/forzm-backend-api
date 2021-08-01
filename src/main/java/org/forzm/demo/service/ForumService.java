package org.forzm.demo.service;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumDto;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForumService {
    ForumDto saveForum(ForumDto forumDto);
    void deleteForum(ForumDto forumDto);
    ForumDto findForumByName(String name);
    Forum mapToForum(ForumDto forumDto);
    ForumDto mapToForumDto(Forum forum);
    List<ForumDto> getAllForums();
}
