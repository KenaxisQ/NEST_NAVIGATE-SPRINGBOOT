package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.utils.property.Directions;
import com.kenaxisq.nestnavigate.utils.property.Furniture;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatePropertyDto {

    // Fields from PropertyDto
    private String title;
    private String type;
    private String propertyCategory;
    private String propertyListingFor;
    private String projectName;
    private String description;
    private Double price;
    private Double advance;
    private Boolean isNegotiable;
    private String primaryContact;
    private String secondaryContact;
    private String mandal;
    private String village;
    private String zip;
    private String address;
    private String media;
    private PropertyStatus status;
    private Boolean isFeatured = false;
    private String amenities;

    // Fields from PgDto
    private Furniture furnitureStatus;
    private String furnitureStatusDescription;

    // Fields from ResidentialPropertyDto
    private Directions facing;
    private Double superBuiltupArea;
    private Double carpetArea;
    private Integer poojaRoom;
    private Integer noOfBedrooms;
    private Integer noOfBathrooms;
    private Integer noOfRooms;
    private Integer noOfBalconies;
    private LocalDateTime moveInDate;

    // Fields from LandDto
    private Double length;
    private Double width;
}