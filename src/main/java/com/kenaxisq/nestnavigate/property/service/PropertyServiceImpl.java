package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.controller.PropertyController;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.user.service.UserServiceImpl;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ErrorResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(Property.class);
    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               UserService userService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<ApiResponse<Property>> getPropertyById(String id) {
        try {
            Property property = propertyRepository.findPropertyById(id);
            if (property != null) {
                return ResponseEntity.ok(ResponseBuilder.success(property, "Property fetched successfully"));
            } else {
                throw new ApiException(ErrorCodes.ERR_PROPERTY_NOT_FOUND.getCode(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getMessage(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getHttpStatus());
            }
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ex.getMessage(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getHttpStatus());
        }
    }


    @Override
    public ResponseEntity<?> getAllProperties() {
        try {
            List<Property> properties = (List<Property>) propertyRepository.findAll();

            if (properties.isEmpty()) {
                throw new ApiException(ErrorCodes.ERR_PROPERTY_NOT_FOUND.getCode(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getMessage(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getHttpStatus());
            }
            logger.info("Properties fetched successfully: " + properties);
            return ResponseEntity.ok(ResponseBuilder.success(properties, "Properties Fetched Successfully"));

        } catch (ApiException ex) {
            logger.error(String.format("Application error occurred: %s", ex.getMessage()));
            ErrorResponse errorResponse = ResponseBuilder.error(ex.getStatus(), ex.getMessage(), ex.getErrorCode());
            return ResponseEntity.status(ex.getStatus()).body(errorResponse);
        } catch (Exception ex) {
            logger.error(String.format("An unexpected error occurred while fetching properties: %s", ex.getMessage()));
            ApiException apiException = new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    "An error occurred while fetching properties: " + ex.getMessage(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getHttpStatus());

            ErrorResponse errorResponse = ResponseBuilder.error(apiException.getStatus(), apiException.getMessage(), apiException.getErrorCode());
            return ResponseEntity.status(apiException.getStatus()).body(errorResponse);
        }
    }
    public String deleteProperty(String id) {
        try{
            Property property = new Property();
            property = propertyRepository.findPropertyById(id);
            if(property!=null){
                propertyRepository.delete(property);
                logger.info("Property deleted successfully: " + property);
                User user = property.getOwner();
                user.setProperties_listed(user.getProperties_listed()-1);
                userService.updateUser(user);
                return "Property deleted successfully";
            }
            else throw new ApiException("ERR_PROPERTY_NOT_FOUND",
                    "No Property with Id: "+ id +" found!!"
                    , HttpStatus.NOT_FOUND);

        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new ApiException("ERR_PROPERTY_DEL",
                    ex.getMessage()
                    , HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Property updateProperty(Property property) {
        try {
            Optional<Property> propertyOptional = propertyRepository.findById(property.getId());
            if (!propertyOptional.isPresent()) {
                throw new ApiException("ERR_PROPERTY_NOT_FOUND",
                        "No Property with Id: " + property.getId() + " found!!", HttpStatus.NOT_FOUND);
            }
            Property existingProperty = propertyOptional.get();

            if (property.getTitle() != null) {
                existingProperty.setTitle(property.getTitle());
            }
            if (property.getType() != null) {
                existingProperty.setType(property.getType());
            }
            if (property.getPropertyCategory() != null) {
                existingProperty.setPropertyCategory(property.getPropertyCategory());
            }
            if (property.getFacing() != null) {
                existingProperty.setFacing(property.getFacing());
            }
            if (property.getPropertyListingFor() != null) {
                existingProperty.setPropertyListingFor(property.getPropertyListingFor());
            }
            if (property.getProjectName() != null) {
                existingProperty.setProjectName(property.getProjectName());
            }
//            if (property.getSubProperty() != null) {
//                existingProperty.setSubProperty(property.getSubProperty());
//            }
            if (property.getFurnitureStatus() != null) {
                existingProperty.setFurnitureStatus(property.getFurnitureStatus());
            }
            if (property.getFurnitureStatusDescription() != null) {
                existingProperty.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
            }
            if (property.getDescription() != null) {
                existingProperty.setDescription(property.getDescription());
            }
            if (property.getSuper_builtup_area() != null) {
                existingProperty.setSuper_builtup_area(property.getSuper_builtup_area());
            }
            if (property.getCarpet_area() != null) {
                existingProperty.setCarpet_area(property.getCarpet_area());
            }
            if (property.getPrice() != null) {
                existingProperty.setPrice(property.getPrice());
            }
            if (property.getAdvance() != null) {
                existingProperty.setAdvance(property.getAdvance());
            }
            if (property.getIsNegotiable() != null) {
                existingProperty.setIsNegotiable(property.getIsNegotiable());
            }
            if (property.getStatus() != null) {
                existingProperty.setStatus(property.getStatus());
            }
            if (property.getIsFeatured() != null) {
                existingProperty.setIsFeatured(property.getIsFeatured());
            }
            if (property.getListedDate() != null) {
                existingProperty.setListedDate(property.getListedDate());
            }
            if (property.getUpdatedDate() != null) {
                existingProperty.setUpdatedDate(property.getUpdatedDate());
            }
            if (property.getExpiryDate() != null) {
                existingProperty.setExpiryDate(property.getExpiryDate());
            }
            if (property.getListedby() != null) {
                existingProperty.setListedby(property.getListedby());
            }
            if (property.getPrimaryContact() != null) {
                existingProperty.setPrimaryContact(property.getPrimaryContact());
            }
            if (property.getState() != null) {
                existingProperty.setState(property.getState());
            }
            if (property.getCountry() != null) {
                existingProperty.setCountry(property.getCountry());
            }
            if (property.getRevenueDivision() != null) {
                existingProperty.setRevenueDivision(property.getRevenueDivision());
            }
            if (property.getMandal() != null) {
                existingProperty.setMandal(property.getMandal());
            }
            if (property.getVillage() != null) {
                existingProperty.setVillage(property.getVillage());
            }
            if (property.getZip() != null) {
                existingProperty.setZip(property.getZip());
            }
            if (property.getLongitude() != null) {
                existingProperty.setLongitude(property.getLongitude());
            }
            if (property.getLatitude() != null) {
                existingProperty.setLatitude(property.getLatitude());
            }
            if (property.getViews() != 0) {
                existingProperty.setViews(property.getViews());
            }
            if (property.getLikes() != 0) {
                existingProperty.setLikes(property.getLikes());
            }

            Property updatedProperty = propertyRepository.save(existingProperty);
            return updatedProperty;
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("ERR_PROPERTY_UPDATE",
                    "Error updating property: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Property saveProperty(Property property, String userId) {
        logger.info("Saving property: " + property.toString());
        if(userService.getUser(userId) == null) {
            throw new ApiException(ErrorCodes.USER_NOT_FOUND);
        }
        if(userService.getUser(userId).getProperties_listing_limit() <= 0) {
            throw new ApiException("ERR_LISTING_LIMIT_EXCEED",
                    "Your Limit for Posting Property Exceeded...",
                    HttpStatus.BAD_REQUEST);
        }
        property.setOwner(userService.getUser(userId));
        List<String> errors = validateProperty(property);
        if (!errors.isEmpty()) {
            String errorMessage = errors.stream().collect(Collectors.joining(", "));
            throw new ApiException("ERR_PROPERTY_VALIDATION",
                    errorMessage, HttpStatus.BAD_REQUEST);
        }

        try {
            Property savedproperty = propertyRepository.save(property);
            logger.info("Property saved successfully: " + savedproperty);
            User user = property.getOwner();
            user.setProperties_listed(user.getProperties_listed()+1);
            user.setProperties_listing_limit(user.getProperties_listing_limit()-1);
            userService. updateUser(user);
            return savedproperty;

        } catch (Exception e) {
            throw new ApiException("ERR_PROPERTY_LISTING", "Error saving property: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> validateProperty(Property property) {
        List<String> errors = new ArrayList<>();

        if (property.getTitle() == null || property.getTitle().isEmpty()) {
            errors.add("Title is mandatory.");
        }
        if (property.getType() == null) {
            errors.add("Property type is mandatory.");
        }
        if (property.getPropertyCategory() == null || property.getPropertyCategory().isEmpty()) {
            errors.add("Property category is mandatory.");
        }
        if (property.getFacing() == null) {
            errors.add("Facing direction is mandatory.");
        }
        if (property.getPropertyListingFor() == null) {
            errors.add("Property listing type is mandatory.");
        }
        if (property.getFurnitureStatus() == null) {
            errors.add("Furniture status is mandatory.");
        }
        if (property.getDescription() == null || property.getDescription().isEmpty()) {
            errors.add("Description is mandatory.");
        }
        if (property.getSuper_builtup_area() == null) {
            errors.add("Super built-up area is mandatory.");
        }
        if (property.getPrice() == null) {
            errors.add("Price is mandatory.");
        }
        if (property.getIsNegotiable() == null) {
            errors.add("Negotiable status is mandatory.");
        }
        if (property.getOwner() == null) {
            errors.add("Owner is mandatory.");
        }
        if (property.getStatus() == null) {
            errors.add("Property status is mandatory.");
        }
        if (property.getListedby() == null) {
            errors.add("Listed by is mandatory.");
        }
        if (property.getPrimaryContact() == null || property.getPrimaryContact().isEmpty()) {
            errors.add("Contact information is mandatory.");
        }
        if (property.getMandal() == null || property.getMandal().isEmpty()) {
            errors.add("Mandal is mandatory.");
        }
        if (property.getVillage() == null || property.getVillage().isEmpty()) {
            errors.add("Village is mandatory.");
        }
        if (property.getZip() == null || property.getZip().isEmpty()) {
            errors.add("Zip code is mandatory.");
        }
        if (property.getState() == null || property.getState().isEmpty()) {
            errors.add("State is mandatory.");
        }
        if (property.getCountry() == null || property.getCountry().isEmpty()) {
            errors.add("Country is mandatory.");
        }
        if (property.getRevenueDivision() == null || property.getRevenueDivision().isEmpty()) {
            errors.add("Revenue division is mandatory.");
        }

        return errors;
    }
    }