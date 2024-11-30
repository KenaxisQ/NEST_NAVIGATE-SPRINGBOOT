package com.kenaxisq.nestnavigate.property_amenities.service;


import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property_amenities.entity.PropertyAmenities;

import java.util.List;

public interface PropertyAmenitiesService {
    List<Property> getPropertiesHavingAmenityId(String id);
    List<Property> getPropertiesHavingAmenityName(String amenityName);
    List<Amenity> getAmenitiesOfPropertyByPropertyId(String propertyId);
    PropertyAmenities createPropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities updatePropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities deletePropertyAmenities(PropertyAmenities propertyAmenities);
    PropertyAmenities getPropertyAmenitiesFromId(String propertyAmenitiesId);
    List<PropertyAmenities> getAllPropertyAmenities();
}
