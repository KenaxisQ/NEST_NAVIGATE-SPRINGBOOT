package com.kenaxisq.nestnavigate.property.dto;

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

    private Directions facing;
    private Furniture furnitureStatus;
    private String furnitureStatusDescription;
    private Double superBuiltUpArea;
    private Double carpetArea;
    private Double length;
    private Double width;
    private Integer poojaRoom;
    private Integer noOfBedrooms;
    private Integer noOfBathrooms;
    private Integer noOfRooms;
    private Integer noOfBalconies;
    private LocalDateTime moveInDate;
}