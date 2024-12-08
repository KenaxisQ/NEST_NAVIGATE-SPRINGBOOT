package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.property.validators.RequiredField;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDto {

    @RequiredField
    private String id;

    @RequiredField(message = "Title is required")
    private String title;

    @RequiredField(message = "Type is required")
    private String type;

    @RequiredField(message = "Property category is required")
    private String propertyCategory;

    @RequiredField(message = "Property listing type is required")
    private String propertyListingFor;

    @RequiredField(message = "Project name is required")
    private String projectName;

    @RequiredField(message = "Description is required")
    private String description;

    @RequiredField(message = "Price is required")
    private Double price;

    @RequiredField(message = "Advance payment amount is required")
    private Double advance;

    @RequiredField(message = "Negotiability status is required")
    private Boolean isNegotiable;

    @RequiredField(message = "Primary contact is required")
    private String primaryContact;

    @RequiredField(message = "Secondary contact is required")
    private String secondaryContact;

    @RequiredField(message = "Mandal is required")
    private String mandal;

    @RequiredField(message = "Village is required")
    private String village;

    @RequiredField(message = "ZIP code is required")
    private String zip;

    @RequiredField(message = "Address is required")
    private String address;

    @RequiredField(message = "Media is required")
    private String media;

//    @RequiredField(message = "Owner information is required")
//    private User owner;
//
//    @RequiredField(message = "ListedBy is required")
//    private String listedBy;

    @RequiredField(message = "Property status is required")
    private PropertyStatus status;

    @RequiredField(message = "Featured status is required")
    private Boolean isFeatured = false;

    private String amenities;
}