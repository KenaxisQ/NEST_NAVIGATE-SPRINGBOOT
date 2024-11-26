package com.kenaxisq.nestnavigate.utils;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ResponseBuilder {
    private static final String API_VERSION = "1.0";

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
//                .requestId(generateRequestId())
//                .metadata(buildMetadata(0L))
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, long executionTime) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
//                .requestId(generateRequestId())
//                .metadata(buildMetadata(executionTime))
                .build();
    }
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
//                .requestId(generateRequestId())
//                .metadata(buildMetadata(executionTime))
                .build();
    }

    public static ErrorResponse error(HttpStatus status, String message, String errorCode) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .success(false)
//                .requestId(generateRequestId())
                .build();
    }
    public static ErrorResponse error(ApiException apiException) {
        return ErrorResponse.builder()
                .status(apiException.getStatus())
                .message(apiException.getMessage())
                .errorCode(apiException.getErrorCode())
                .timestamp(LocalDateTime.now())
                .success(false)
//                .requestId(generateRequestId())
                .build();
    }

    private static String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private static MetaData buildMetadata(long executionTime) {
        return MetaData.builder()
                .version(API_VERSION)
                .executionTimeMs(executionTime)
                .build();
    }
}