package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.property.validators.RequiredField;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PgDto extends PropertyDto {

    @RequiredField(message = "Furniture status is required")
    private Furniture furnitureStatus;

    @RequiredField(message = "Furniture status description is required")
    private String furnitureStatusDescription;
}
