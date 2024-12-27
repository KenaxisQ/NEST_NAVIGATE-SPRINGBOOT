package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.property.validators.RequiredField;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommercialPropertyDto extends PropertyDto
{
    @RequiredField(message = "Facing direction is required")
    private Directions facing;

    @RequiredField(message = "Furniture status is required")
    private Furniture furnitureStatus;

    @RequiredField(message = "Furniture status description is required")
    private String furnitureStatusDescription;

    @RequiredField(message = "Super built-up area is required")
    private Double superBuiltupArea;

    @RequiredField(message = "Carpet area is required")
    private Double carpetArea;

    @RequiredField(message = "Length is required")
    private Double length;

    @RequiredField(message = "Width is required")
    private Double width;

    @RequiredField(message = "Move-in date is required")
    private LocalDateTime moveInDate;
}
