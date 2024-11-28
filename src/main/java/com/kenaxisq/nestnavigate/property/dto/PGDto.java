package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PGDto {
    private String title;
    private String type;
    private PropertyCategory propertyCategory = PropertyCategory.PG;
    private PropertyListingType propertyListingFor= PropertyListingType.RENT;
    private String ProjectName;
    private String description;
    private Furniture furnitureStatus;
    private String furnitureStatusDescription;
    private Double price;
    private Double advance;
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
}
