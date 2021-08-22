package org.forzm.demo.controllerAdvice;

import org.forzm.demo.exception.ForumExistsException;
import org.forzm.demo.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class ForumControllerAdvice {

    @ExceptionHandler({ForumExistsException.class})
    public ResponseEntity<ApiError> handleForumExistsException(WebRequest request, RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .httpStatus(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
