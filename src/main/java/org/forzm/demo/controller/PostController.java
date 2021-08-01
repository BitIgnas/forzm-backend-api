package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumDto;
import org.forzm.demo.dto.PostDto;
import org.forzm.demo.model.Post;
import org.forzm.demo.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/save").toUriString());
        return ResponseEntity.created(location).body(postService.addPost(postDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAllPosts());
    }

    @GetMapping("/{title}")
    public ResponseEntity<PostDto> getPostByTitle(@PathVariable("title") String title) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostByTitle(title));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestBody @Valid PostDto postDto) {
        postService.deletePost(postDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
