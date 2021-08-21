package org.forzm.demo.service;

import org.forzm.demo.dto.CommentRequestDto;
import org.forzm.demo.dto.CommentResponseDto;
import org.forzm.demo.model.Comment;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> getAllPostComments(String title, Long postId);
    Long getUserCommentCount(String username);
    Comment mapToComment(CommentRequestDto commentRequestDto);
    CommentResponseDto mapToCommentResponseDto(Comment comment);
}
