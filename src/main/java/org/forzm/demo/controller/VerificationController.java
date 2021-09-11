package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user/{username}/email/{email}/resend")
    public ResponseEntity<?> resendVerificationEmail(@PathVariable("username") String username,
                                                     @PathVariable String email) {
        verificationService.resendVerificationMail(username, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
