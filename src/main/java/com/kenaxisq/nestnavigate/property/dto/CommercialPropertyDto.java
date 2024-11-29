package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommercialPropertyDto extends PropertyDto
{
    private Directions facing;
    private Furniture furnitureStatus;
    private String furnitureStatusDescription;
    private Double superBuiltUpArea;
    private Double carpetArea;
    private Double length;
    private Double width;
    private LocalDateTime moveInDate;
}
