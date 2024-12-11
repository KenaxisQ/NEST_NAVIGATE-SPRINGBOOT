package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.property.validators.RequiredField;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LandDto extends PropertyDto{

    @RequiredField(message = "Facing direction is required")
    private Directions facing;

    @RequiredField(message = "Super built-up area is required")
    private Double superBuiltupArea;

    @RequiredField(message = "Length is required")
    private Double length;

    @RequiredField(message = "Width is required")
    private Double width;
}

