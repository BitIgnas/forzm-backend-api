package org.forzm.demo.controllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.forzm.demo.exception.ForumException;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.exception.UserExistsException;
import org.forzm.demo.exception.VerificationTokenException;
import org.forzm.demo.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.LimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ForumException.class, PostException.class, VerificationTokenException.class})
    public ResponseEntity<ApiError> handleEntityException(WebRequest request, RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserExistsException.class})
    public ResponseEntity<ApiError> handleUserExist(WebRequest request, RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .httpStatus(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ClassCastException.class})
    public ResponseEntity<ApiError> handleInvalidJwt(WebRequest request, Exception exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ApiError> handleJwtExpiredException(WebRequest request, ExpiredJwtException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }


}
