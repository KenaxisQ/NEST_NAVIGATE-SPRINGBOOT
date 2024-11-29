package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;

public class PropertyDto {
    private String title;
    private String type;
    private String propertyCategory;
    private String propertyListingFor;
    private String ProjectName;
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
    private User owner;
    private PropertyStatus status;
    private Boolean isFeatured = false;
}
