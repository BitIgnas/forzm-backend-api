package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.CommentRequestDto;
import org.forzm.demo.dto.CommentResponseDto;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.model.Comment;
import org.forzm.demo.model.Post;
import org.forzm.demo.repository.CommentRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto) {
        Post post = postRepository.findPostByTitleAndId(commentRequestDto.getPostTitle(), commentRequestDto.getPostId())
                .orElseThrow(() -> new PostException("No posts where found"));

        Comment comment = mapToComment(commentRequestDto);
        comment.setUser(authService.getCurrentUser());
        comment.setPost(post);
        comment.setDateReplied(Instant.now());
        return mapToCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public List<CommentResponseDto> getAllPostComments(String title, Long postId) {
        Post post = postRepository.findPostByTitleAndId(title, postId)
                .orElseThrow(() -> new PostException("No post where found"));

        return commentRepository.findAllByPost(post).stream()
                .map(this::mapToCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment mapToComment(CommentRequestDto commentRequestDto) {
        return modelMapper.map(commentRequestDto, Comment.class);
    }

    @Override
    public CommentResponseDto mapToCommentResponseDto(Comment comment) {
        return modelMapper.map(comment, CommentResponseDto.class);
    }
}
