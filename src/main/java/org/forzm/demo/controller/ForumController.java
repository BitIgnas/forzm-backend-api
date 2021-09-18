package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.service.ForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
@AllArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @PostMapping("/save")
    public ResponseEntity<ForumResponseDto> createForum(@RequestBody @Valid ForumRequestDto forumRequestDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/forum/save").toUriString());
        return ResponseEntity.created(location).body(forumService.createForum(forumRequestDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ForumResponseDto>> getAllForums() {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.getAllForums());
    }

    @GetMapping("/{forumName}/user/{username}")
    public ResponseEntity<Boolean> checkIfForumIsUsers(@PathVariable("forumName") String forumName,
                                                       @PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.checkIfForumIsUsers(forumName, username));
    }

    @GetMapping("/{forumName}")
    public ResponseEntity<ForumResponseDto> getForumByName(@PathVariable("forumName") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findForumByName(name));
    }

    @GetMapping("/type/{forumGameType}")
    public ResponseEntity<List<ForumResponseDto>> getForumsByForumGameType(@PathVariable("forumGameType") ForumGameType forumGameType) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findForumsByGameType(forumGameType));
    }

    @GetMapping("/{forumName}/search")
    public ResponseEntity<List<ForumResponseDto>> searchForumsByName(@PathVariable("forumName") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findForumByNameIgnoreCase(name));
    }

    @GetMapping("/{username}/forums")
    public ResponseEntity<List<ForumResponseDto>> findUserForumsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findUserForumsByUsername(username));
    }

    @DeleteMapping("/{forumName}/delete")
    public ResponseEntity<?> deleteForum(@PathVariable("forumName") String forumName) {
        forumService.deleteForum(forumName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
