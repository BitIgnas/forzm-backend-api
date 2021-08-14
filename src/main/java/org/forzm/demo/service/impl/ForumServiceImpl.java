package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.ForumExistsException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.ForumService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
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
    public ForumResponseDto createForum(ForumRequestDto forumRequestDto) {
        checkIfForumExist(forumRequestDto.getName());
        Forum forum = mapToForum(forumRequestDto);
        forum.setUser(authService.getCurrentUser());
        forum.setCreated(Instant.now());

        return mapToForumResponseDto(forumRepository.save(forum));
    }

    @Override
    @Transactional
    public List<ForumResponseDto> getAllForums() {
        return forumRepository.getAllByOrderByPostsDesc().stream()
                .map(this::mapToForumResponseDto)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteForum(ForumRequestDto forumRequestDto) {
        forumRepository.delete(mapToForum(forumRequestDto));
    }

    @Override
    public void checkIfForumExist(String forumName) {
        Optional<Forum> forumOptional = forumRepository.getForumByName(forumName);
        forumOptional.ifPresent(forum -> { throw new ForumExistsException("Forum already exists");});
    }

    @Override
    public ForumResponseDto findForumByName(String name) {
        return mapToForumResponseDto(forumRepository.getForumByName(name)
                .orElseThrow(() -> new ForumException("Forum was not found")));
    }

    @Override
    public ForumResponseDto mapToForumResponseDto(Forum forum) {
        return modelMapper.map(forum, ForumResponseDto.class);
    }

    @Override
    public Forum mapToForum(ForumRequestDto forumRequestDto) {
        return modelMapper.map(forumRequestDto, Forum.class);
    }


}
