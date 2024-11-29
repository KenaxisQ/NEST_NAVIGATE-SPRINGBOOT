package com.kenaxisq.nestnavigate.custom_exceptions;

import com.kenaxisq.nestnavigate.property.dto.PropertyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
//
//import javax.validation.ConstraintViolation;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException{
    private final HttpStatus status;
    private final String errorCode;
//    private T data;
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
//    public ApiResponse(Set<ConstraintViolation<PropertyDto>> violations) {
//        super("Validation failed");
//        this.status = HttpStatus.BAD_REQUEST
//        this.data = (T) violations;
//    }
}
