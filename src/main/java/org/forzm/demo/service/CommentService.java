package org.forzm.demo.service;

import org.forzm.demo.dto.CommentDto;
import org.forzm.demo.dto.PostDto;
import org.forzm.demo.model.Comment;
import org.forzm.demo.model.Post;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentDto commentDto);
    List<CommentDto> getAllPostComments(String title);
    Comment mapToComment(CommentDto commentDto);
    CommentDto mapToCommentDto(Comment comment);
}
