package com.kenaxisq.nestnavigate.property_amenities.service;


import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.amenity.service.AmenityService;
import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property_amenities.entity.PropertyAmenities;
import com.kenaxisq.nestnavigate.property_amenities.repository.PropertyAmenitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyAmenitiesServiceImpl implements PropertyAmenitiesService {

    private final PropertyAmenitiesRepository propertyAmenitiesRepository;
    private final AmenityService amenityService;
    private final PropertyService propertyService;

    @Autowired
    public PropertyAmenitiesServiceImpl(PropertyAmenitiesRepository propertyAmenitiesRepository, AmenityService amenityService, PropertyService propertyService) {
        this.propertyAmenitiesRepository = propertyAmenitiesRepository;
        this.amenityService = amenityService;
        this.propertyService = propertyService;
    }

    @Override
    public List<Property> getPropertiesHavingAmenityId(String amenityId) {
        try {
            Amenity amenity = amenityService.getAmenityFromId(amenityId);
            List<PropertyAmenities> propertyAmenities = propertyAmenitiesRepository.findAllByAmenityId(amenity);
            List<Property> properties = propertyAmenities.stream()
                    .map(PropertyAmenities::getPropertyId)
                    .collect(Collectors.toList());
            if (properties.isEmpty()) {
                throw new ApiException("ERR_PROPERTY_AMENITIES", "No properties found with amenity " + amenity.getName(), HttpStatus.NOT_FOUND);
            }

            return properties;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public List<Property> getPropertiesHavingAmenityName(String amenityName) {
        try {
            // Fetch the amenity using the name
            Amenity amenity = amenityService.getAmenityFromName(amenityName);

            // Fetch property-amenities relations
            List<PropertyAmenities> propertyAmenities = propertyAmenitiesRepository.findAllByAmenityId(amenity);

            // Map propertyAmenities to property list
            List<Property> properties = propertyAmenities.stream()
                    .map(PropertyAmenities::getPropertyId)
                    .collect(Collectors.toList());

            // Check if the properties list is empty and throw an exception if necessary
            if (properties.isEmpty()) {
                throw new ApiException("ERR_PROPERTY_AMENITIES", "No properties found with amenity " + amenity.getName(), HttpStatus.NOT_FOUND);
            }

            return properties;
        } catch (ApiException e) {
            // Re-throw known exceptions
            throw e;
        } catch (Exception e) {
            // Handle any other unforeseen exceptions
            throw new ApiException("ERR_PROPERTY_AMENITIES", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<Amenity> getAmenitiesOfPropertyByPropertyId(String propertyId) {
        try {
            Property property = propertyService.getPropertyById(propertyId);
            List<PropertyAmenities> propertyAmenities = propertyAmenitiesRepository.findAllByPropertyId(property);
            if (propertyAmenities.isEmpty()) {
                throw new ApiException("ERR_PROPERTY_AMENITIES", "No amenities found for the property with ID: " + propertyId, HttpStatus.NOT_FOUND);
            }
            List<Amenity> amenities = propertyAmenities.stream()
                    .map(PropertyAmenities::getAmenityId)
                    .collect(Collectors.toList());
            return amenities;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public PropertyAmenities createPropertyAmenities(PropertyAmenities propertyAmenities) {
        try {
            Property property = propertyAmenities.getPropertyId();
            Amenity amenity = propertyAmenities.getAmenityId();

            if (property == null) {
                throw new ApiException("ERR_PROPERTY_NULL", "Property cannot be null", HttpStatus.BAD_REQUEST);
            }

            if (amenity == null) {
                throw new ApiException("ERR_AMENITY_NULL", "Amenity cannot be null", HttpStatus.BAD_REQUEST);
            }
            List<PropertyAmenities> existingAmenities = propertyAmenitiesRepository.findByPropertyIdAndAmenityId(property, amenity);

            if (!existingAmenities.isEmpty()) {
                throw new ApiException("ERR_AMENITY_EXISTS", "Property already has this amenity", HttpStatus.CONFLICT);
            }
            PropertyAmenities propertyAmenities1 = new PropertyAmenities(propertyAmenities.getPropertyId(),propertyAmenities.getAmenityId());
            PropertyAmenities savedPropertyAmenity = new PropertyAmenities();
            propertyAmenitiesRepository.save(propertyAmenities1);
            propertyAmenitiesRepository.findByPropertyIdAndAmenityId(propertyAmenities1.getPropertyId(), propertyAmenities1.getAmenityId());
            return savedPropertyAmenity;

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES_CREATION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PropertyAmenities updatePropertyAmenities(PropertyAmenities propertyAmenities) {
        try{
            Property property = propertyAmenities.getPropertyId();
            Amenity amenity = propertyAmenities.getAmenityId();
            if (property == null) {
                throw new ApiException("ERR_PROPERTY_NULL", "Property cannot be null", HttpStatus.BAD_REQUEST);
            }
            if (amenity == null) {
                throw new ApiException("ERR_AMENITY_NULL", "Amenity cannot be null", HttpStatus.BAD_REQUEST);
            }
            List<PropertyAmenities> existingAmenities = propertyAmenitiesRepository.findByPropertyIdAndAmenityId(property, amenity);
            if (existingAmenities.isEmpty()) {
                throw new ApiException("ERR_AMENITY_EXISTS", "Property already has this amenity", HttpStatus.CONFLICT);
            }
            PropertyAmenities propertyAmenities1 = new PropertyAmenities(propertyAmenities.getPropertyId(),propertyAmenities.getAmenityId());
            PropertyAmenities savedPropertyAmenity = new PropertyAmenities();
            propertyAmenitiesRepository.save(propertyAmenities1);
            propertyAmenitiesRepository.findByPropertyIdAndAmenityId(propertyAmenities1.getPropertyId(), propertyAmenities1.getAmenityId());
            return savedPropertyAmenity;
        }
        catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES_UPDATE", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PropertyAmenities deletePropertyAmenities(PropertyAmenities propertyAmenities) {
        try {
            // Validate if the propertyAmenities object is not null
            if (propertyAmenities == null) {
                throw new ApiException("ERR_PROPERTY_AMENITIES_NULL", "PropertyAmenities cannot be null", HttpStatus.BAD_REQUEST);
            }

            // Perform deletion using ID if it is provided
            String propertyAmenitiesId = propertyAmenities.getId();
            if (propertyAmenitiesId != null) {
                propertyAmenitiesRepository.deleteById(propertyAmenitiesId);
                return propertyAmenities; // Return the deleted object details as confirmation
            }

            // Otherwise, proceed to validate Property and Amenity fields
            Property property = propertyAmenities.getPropertyId();
            Amenity amenity = propertyAmenities.getAmenityId();

            if (property == null) {
                throw new ApiException("ERR_PROPERTY_NULL", "Property cannot be null", HttpStatus.BAD_REQUEST);
            }

            if (amenity == null) {
                throw new ApiException("ERR_AMENITY_NULL", "Amenity cannot be null", HttpStatus.BAD_REQUEST);
            }

            // Check if the property already has the amenity and delete them
            List<PropertyAmenities> existingAmenities = propertyAmenitiesRepository.findByPropertyIdAndAmenityId(property, amenity);

            if (!existingAmenities.isEmpty()) {
                for (PropertyAmenities pa : existingAmenities) {
                    propertyAmenitiesRepository.delete(pa);
                }
            }

            return propertyAmenities; // Return the object as confirmation

        } catch (ApiException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES_DELETION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PropertyAmenities getPropertyAmenitiesFromId(String propertyAmenitiesId) {
        try{
            PropertyAmenities propertyAmenities = propertyAmenitiesRepository.findById(propertyAmenitiesId).orElse(null);
            if (propertyAmenities== null) {
                throw new ApiException("ERR_PROPERTY_AMENITIES_NOT_FOUND", "PropertyAmenities not found with ID: " + propertyAmenitiesId, HttpStatus.NOT_FOUND);
            }
            return propertyAmenities;
        }
        catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<PropertyAmenities> getAllPropertyAmenities() {
        try{
            List<PropertyAmenities> propertyAmenitiesList = (List<PropertyAmenities>) propertyAmenitiesRepository.findAll();
            if (propertyAmenitiesList.isEmpty()) {
                throw new ApiException("ERR_PROPERTY_AMENITIES_NOT_FOUND", "No PropertyAmenities found", HttpStatus.NOT_FOUND);
            }
            return propertyAmenitiesList;
        }
        catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_AMENITIES_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
