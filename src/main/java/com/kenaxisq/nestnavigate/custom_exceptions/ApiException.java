package com.kenaxisq.nestnavigate.custom_exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException{
    private final HttpStatus status;
    private final String errorCode;
    public ApiException(ErrorCodes Error){

        super(Error.getMessage());
        this.errorCode = Error.getCode();
        this.status = Error.getHttpStatus();

    }
    public ApiException(String errorCode,String message, HttpStatus status ){

        super(message);
        this.errorCode = errorCode;
        this.status = status;

    }
}
