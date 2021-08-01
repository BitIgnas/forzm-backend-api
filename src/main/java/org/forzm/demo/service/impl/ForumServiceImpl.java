package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumDto;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.ForumService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final ForumRepository forumRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @Override
    @Transactional
    public ForumDto saveForum(ForumDto forumDto) {
        Forum forum = mapToForum(forumDto);
        forum.setUser(authService.getCurrentUser());
        forum.setCreated(Instant.now());

        return mapToForumDto(forumRepository.save(forum));
    }

    @Override
    @Transactional
    public List<ForumDto> getAllForums() {
        return forumRepository.findAll().stream().map(this::mapToForumDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteForum(ForumDto forumDto) {
        forumRepository.delete(mapToForum(forumDto));
    }

    @Override
    public ForumDto findForumByName(String name) {
        Forum forum =  forumRepository.getForumByName(name).orElseThrow(() -> new ForumException("Forum was not found"));

        return mapToForumDto(forum);
    }

    @Override
    public Forum mapToForum(ForumDto forumDto) {
        return modelMapper.map(forumDto, Forum.class);
    }

    @Override
    public ForumDto mapToForumDto(Forum forum) {
        return modelMapper.map(forum, ForumDto.class);
    }
}
