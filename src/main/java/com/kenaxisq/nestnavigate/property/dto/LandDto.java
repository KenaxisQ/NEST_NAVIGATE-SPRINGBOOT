package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LandDto
{
    private String title;
    private String type;
    private PropertyCategory propertyCategory = PropertyCategory.LAND;
    private Directions facing;
    private PropertyListingType propertyListingFor;
    private String projectName;
//    private Furniture furnitureStatus;
//    private String furnitureStatusDescription;
    private String description;
    private Double superBuiltUpArea;
//    private Double carpetArea;
    private Double price;
    private Double advance;
    private Double length;
    private Double width;
    private Boolean isNegotiable;
    private User owner;
    private PropertyStatus status;
    private Boolean isFeatured = false;
    private LocalDateTime updatedDate = LocalDateTime.now();
    private String primaryContact;
    private String secondaryContact;
    private String mandal;
    private String village;
    private String zip;
    private String address;
    private String media;
//    private LocalDateTime moveInDate;
}

