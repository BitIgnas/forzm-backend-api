package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.CommentDto;
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
    public ResponseEntity<CommentDto> addComment(@RequestBody @Valid CommentDto commentDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/comment/save").toUriString());
        return ResponseEntity.created(location).body(commentService.addComment(commentDto));
    }

    @GetMapping("/{postTitle}")
    public ResponseEntity<List<CommentDto>> getAllPostComments(@PathVariable("postTitle") String title) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllPostComments(title));
    }
}
