package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verify")
@AllArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping("/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String verificationToken) {
        verificationService.verifyUser(verificationToken);
        return ResponseEntity.status(HttpStatus.OK).body("User activated");
    }
}
