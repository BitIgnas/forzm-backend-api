package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.ForumExistsException;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.model.Post;
import org.forzm.demo.repository.CommentRepository;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
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
@Transactional
public class ForumServiceImpl implements ForumService {

    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @Override
    @Transactional
    public ForumResponseDto createForum(ForumRequestDto forumRequestDto) {
        Optional<Forum> forumOptional = forumRepository.getForumByName(forumRequestDto.getName());
        forumOptional.ifPresent(forum -> { throw new ForumExistsException("Forum already exists");});
        Forum forum = mapToForum(forumRequestDto);
        forum.setUser(authService.getCurrentUser());
        forum.setCreated(Instant.now());

        return mapToForumResponseDto(forumRepository.save(forum));
    }

    @Override
    public List<ForumResponseDto> findForumByNameIgnoreCase(String name) {
        return forumRepository.getAllByNameContainingIgnoreCase(name).stream()
                .map(this::mapToForumResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ForumResponseDto> getAllForums() {
        return forumRepository.getAllByOrderByPostsAsc().stream()
                .map(this::mapToForumResponseDto)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteForum(String name) {
        Forum forum = forumRepository.getForumByName(name)
                .orElseThrow(() -> new ForumException("No forum was found"));

        postRepository.findAllByForum(forum).forEach(commentRepository::deleteAllByPost);
        postRepository.deleteAllByForum(forum);
        forumRepository.delete(forum);
    }

    @Override
    public List<ForumResponseDto> findForumsByGameType(ForumGameType gameType) {
        return forumRepository.findAllByForumGameType(gameType).stream()
                .map(this::mapToForumResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfForumIsUsers(String forumName, String username) {
        return forumRepository.findForumsByNameAndUserUsername(forumName, username).isPresent();
    }

    @Override
    public List<ForumResponseDto> findUserForumsByUsername(String username) {
        return forumRepository.findForumsByUser_Username(username).stream()
                .map(this::mapToForumResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ForumResponseDto findForumByName(String name) {
        return mapToForumResponseDto(forumRepository.getForumByName(name)
                .orElseThrow(() -> new ForumException("Forum was not found")));
    }

    @Override
    public ForumResponseDto mapToForumResponseDto(Forum forum) { return modelMapper.map(forum, ForumResponseDto.class); }

    @Override
    public Forum mapToForum(ForumRequestDto forumRequestDto) {
        return modelMapper.map(forumRequestDto, Forum.class);
    }
}
