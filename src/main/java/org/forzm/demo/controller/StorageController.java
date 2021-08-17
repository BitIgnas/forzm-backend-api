package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.forzm.demo.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/storage")
@AllArgsConstructor
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/user/{username}/upload")
    public ResponseEntity<String> uploadUserFile(@RequestParam("file") MultipartFile multipartFile,
                                                 @PathVariable("username") String username) {
        URI location = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/storage/save/file").toUriString());
        storageService.saveUserProfile(multipartFile, username);
        return ResponseEntity.created(location).body("Image saved");
    }

    @PostMapping("/forum/{forumName}/upload")
    public ResponseEntity<String> uploadForumFile(@RequestParam("file") MultipartFile multipartFile,
                                                  @PathVariable("forumName") String forumTitle) {
        URI location = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/storage/save/file").toUriString());
        storageService.saveForumImage(multipartFile, forumTitle);
        return ResponseEntity.created(location).body("Image saved");
    }
}
