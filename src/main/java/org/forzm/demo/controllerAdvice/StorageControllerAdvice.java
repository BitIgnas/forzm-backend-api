package org.forzm.demo.controllerAdvice;

import org.forzm.demo.controller.StorageController;
import org.forzm.demo.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;

@ControllerAdvice
public class StorageControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadException(WebRequest request, MaxUploadSizeExceededException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value())
                .httpStatus(HttpStatus.PAYLOAD_TOO_LARGE)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
