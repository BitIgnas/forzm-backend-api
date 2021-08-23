package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.PostRequestDto;
import org.forzm.demo.dto.PostResponseDto;
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
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/save").toUriString());
        return ResponseEntity.created(location).body(postService.addPost(postRequestDto));
    }

    @GetMapping("/{forum}/{postType}/all")
    public ResponseEntity<List<PostResponseDto>> getAllPostsByForumAndByType(@PathVariable("forum") String forumName,
                                                                             @PathVariable("postType") String forumType) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAllPostsByForumNameAndByPostType(forumName, forumType));
    }

    @GetMapping("/{title}/{id}")
    public ResponseEntity<PostResponseDto> getPostByTitle(@PathVariable("title") String title,
                                                          @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByPostTitleAndId(title, id));
    }

    @GetMapping("/{forumName}/posts/count")
    public ResponseEntity<Long> getPostCountByForumName(@PathVariable("forumName") String forumName) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.countAllForumPosts(forumName));
    }

    @GetMapping("/user/{username}/posts")
    public ResponseEntity<List<PostResponseDto>> getAllUserPostsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllUserPostsByUsername(username));
    }

    @GetMapping("/user/{username}/posts/count")
    public ResponseEntity<Long> getPostCountByUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.countAllUserPosts(username));
    }

    @GetMapping("/user/{username}/posts/recent")
    public ResponseEntity<List<PostResponseDto>> getUserFiveRecentCreatedPosts(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserFiveRecentCreatedPosts(username));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestBody @Valid PostRequestDto postRequestDto) {
        postService.deletePost(postRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
