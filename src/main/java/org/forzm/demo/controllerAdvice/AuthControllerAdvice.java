package org.forzm.demo.controllerAdvice;

import org.forzm.demo.exception.UserExistsException;
import org.forzm.demo.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> handleUserExistsException(WebRequest request, RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .httpStatus(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(WebRequest request, AuthenticationException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
