package com.kenaxisq.nestnavigate.custom_exceptions;

import com.kenaxisq.nestnavigate.utils.ErrorResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
//import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorResponse error = ResponseBuilder.error(
                ex.getStatus(),
                ex.getMessage(),
                ex.getErrorCode()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .success(false)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
//        ErrorResponse error = ResponseBuilder.error(HttpStatus.UNAUTHORIZED,
//                ex.getMessage(),"INVALID_TOKEN"
//        );
//        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(FileMissingException.class)
    public ResponseEntity<String> handleFileMissingException(FileMissingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}

