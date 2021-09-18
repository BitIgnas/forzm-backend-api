package org.forzm.demo.controllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import org.forzm.demo.exception.ExpiredVerificationTokenException;
import org.forzm.demo.exception.PostException;
import org.forzm.demo.exception.RefreshTokenException;
import org.forzm.demo.exception.VerificationTokenException;
import org.forzm.demo.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class TokenControllerAdvice extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiError> handleInvalidRefreshToken(WebRequest request, RuntimeException exception) {
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(VerificationTokenException.class)
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

    @ExceptionHandler(ExpiredVerificationTokenException.class)
    public ResponseEntity<ApiError> handleExpiredVerificationToken(WebRequest request, RuntimeException exception) {
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
