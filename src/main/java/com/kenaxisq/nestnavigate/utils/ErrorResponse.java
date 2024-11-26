package com.kenaxisq.nestnavigate.utils;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import com.kenaxisq.nestnavigate.utils.ApiResponse;

@Data
@SuperBuilder
public class ErrorResponse extends ApiResponse<Object> {
    private String errorCode;
    private String debugMessage;
    private String supportReference;
}
