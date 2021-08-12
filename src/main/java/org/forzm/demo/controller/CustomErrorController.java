package org.forzm.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(final HttpServletRequest request, final HttpServletResponse response) throws Throwable {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(request.getAttribute("javax.servlet.error.exception"));
    }
}

