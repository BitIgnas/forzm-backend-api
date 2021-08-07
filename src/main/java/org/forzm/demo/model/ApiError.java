package org.forzm.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private Integer statusCode;
    private HttpStatus httpStatus;
    private String message;
    private String description;
    private Instant timestamp;
}
