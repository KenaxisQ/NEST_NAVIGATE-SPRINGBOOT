package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.property.validators.RequiredField;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidentialPropertyDto extends PropertyDto {

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

    @RequiredField(message = "Pooja room count is required")
    private Integer poojaRoom;

    @RequiredField(message = "Number of bedrooms is required")
    private Integer noOfBedrooms;

    @RequiredField(message = "Number of bathrooms is required")
    private Integer noOfBathrooms;

    @RequiredField(message = "Number of rooms is required")
    private Integer noOfRooms;

    @RequiredField(message = "Number of balconies is required")
    private Integer noOfBalconies;

    @RequiredField(message = "Move-in date is required")
    private LocalDateTime moveInDate;
}