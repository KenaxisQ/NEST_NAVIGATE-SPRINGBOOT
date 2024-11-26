package com.kenaxisq.nestnavigate.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kenaxisq.nestnavigate.utils.MetaData;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private List<String> errors;
    private String requestId;  // For tracking requests
    private MetaData metadata;

    @Builder.Default
    private boolean success = true;

}