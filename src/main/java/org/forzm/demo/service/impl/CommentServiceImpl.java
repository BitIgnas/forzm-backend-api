package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.CommentDto;
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
    public CommentDto addComment(CommentDto commentDto) {
//        Post post = postRepository.findByTitle(commentDto.getPostTitle())
//                .orElseThrow(() -> new PostException("No posts where found"));
//        Comment comment = mapToComment(commentDto);
//        comment.setUser(authService.getCurrentUser());
//        comment.setPost(post);
//        comment.setDateReplied(Instant.now());
//
//        return mapToCommentDto(commentRepository.save(comment));
        return null;
    }

    @Override
    @Transactional
    public List<CommentDto> getAllPostComments(String title) {
////        Post post = postRepository.findByTitle(title)
////                .orElseThrow(() -> new PostException("No posts where found"));
//
//        return commentRepository.findAllByPost(post).stream()
//                .map(this::mapToCommentDto)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public Comment mapToComment(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }

    @Override
    public CommentDto mapToCommentDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }
}
