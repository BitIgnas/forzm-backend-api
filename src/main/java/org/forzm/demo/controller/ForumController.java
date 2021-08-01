package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.ForumDto;
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
    public ResponseEntity<ForumDto> createForum(@RequestBody @Valid ForumDto forumDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/forum/save").toUriString());
        return ResponseEntity.created(location).body(forumService.saveForum(forumDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ForumDto>> getAllForums() {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.getAllForums());
    }

    @GetMapping("/{forumName}")
    public ResponseEntity<ForumDto> getForumByName(@PathVariable("forumName") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(forumService.findForumByName(name));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteForum(@RequestBody @Valid ForumDto forumDto) {
        forumService.deleteForum(forumDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
