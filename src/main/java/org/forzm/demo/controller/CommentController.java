package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.CommentRequestDto;
import org.forzm.demo.dto.CommentResponseDto;
import org.forzm.demo.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentResponseDto> addComment(@RequestBody @Valid CommentRequestDto commentRequestDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/comment/save").toUriString());
        return ResponseEntity.created(location).body(commentService.addComment(commentRequestDto));
    }

    @GetMapping("/{postTitle}/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getAllPostComments(@PathVariable("postTitle") String title,
                                                                       @PathVariable("postId") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllPostComments(title, id));
    }
}
