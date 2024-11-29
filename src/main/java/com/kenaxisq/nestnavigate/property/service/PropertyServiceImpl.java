package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.controller.PropertyController;
import com.kenaxisq.nestnavigate.property.dto.*;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.mapper.PropertyDtoMapper;
import com.kenaxisq.nestnavigate.property.mapper.PropertyMapper;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.validators.CommonValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.user.service.UserServiceImpl;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ErrorResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import com.kenaxisq.nestnavigate.utils.property.PropertyCategory;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Property getPropertyById(String id) {
        try {
            Property property = propertyRepository.findPropertyById(id);
            if (property != null) {
                return property;
            } else {
                throw new ApiException(ErrorCodes.ERR_PROPERTY_NOT_FOUND);
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
    public List<Property> getAllProperties() {
        try {
            List<Property> properties = (List<Property>) propertyRepository.findAll();

            if (properties.isEmpty()) {
                throw new ApiException(ErrorCodes.ERR_PROPERTY_NOT_FOUND.getCode(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getMessage(),
                        ErrorCodes.ERR_PROPERTY_NOT_FOUND.getHttpStatus());
            }
            logger.info("Properties fetched successfully: " + properties);
            return properties;

        } catch (ApiException ex) {
            logger.error(String.format("An unexpected error occurred while fetching properties: %s", ex.getMessage()));
           throw ex;
        } catch (Exception ex) {
            logger.error(String.format("An unexpected error occurred while fetching properties: %s", ex.getMessage()));
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    "An error occurred while fetching properties: " + ex.getMessage(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getHttpStatus());
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
            if (property.getSuperBuiltupArea() != null) {
                existingProperty.setSuperBuiltupArea(property.getSuperBuiltupArea());
            }
            if (property.getCarpetArea() != null) {
                existingProperty.setCarpetArea(property.getCarpetArea());
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

    public Property postProperty(AggregatePropertyDto propertyDto, String userId) {
        logger.info("Received Property Data: " + propertyDto.toString());
        try {
            userService.getUser(userId);
            if (userService.getUser(userId).getProperties_listing_limit() <= 0) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            boolean isValidCategory = false;
            for (PropertyCategory category : PropertyCategory.values()) {
                if (category.name().equalsIgnoreCase(propertyDto.getPropertyCategory())) {
                    isValidCategory = true;
                    break;
                }
            }
            if (!isValidCategory) {
                throw new ApiException(ErrorCodes.INVALID_PROPERTY_CATEGORY);
            }
            Property property = validateAndReturnPropertyEntity(propertyDto);
            property.setListedby(getAuthorityOfUser(userId).toString());
            property.setOwner(userService.getUser(userId));
            logger.info("Saving property: " + property.toString());
            Property savedProperty = propertyRepository.save(property);
            userService.getUser(userId).setProperties_listed(userService.getUser(userId).getProperties_listed()+1);
            userService.getUser(userId).setProperties_listing_limit(userService.getUser(userId).getProperties_listing_limit()-1);
            return savedProperty;
        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new ApiException("ERR_PROPERTY_POST",
                    "Error saving property: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    private Property validateAndReturnPropertyEntity(AggregatePropertyDto propertyDto) throws ApiException {
        if (propertyDto.getPropertyCategory().equalsIgnoreCase("PG")) {
            PgDto pg = PropertyDtoMapper.mapToPgDto(propertyDto);
            validatePropertyDto(pg);
            return PropertyMapper.mapDtoToEntity(pg, Property.class);
        } else if (propertyDto.getPropertyCategory().equalsIgnoreCase("LAND")) {
            LandDto land = PropertyDtoMapper.mapToLandDto(propertyDto);
            validatePropertyDto(land);
            return PropertyMapper.mapDtoToEntity(land, Property.class);
        }else if (propertyDto.getPropertyCategory().equalsIgnoreCase("RESIDENTIAL")) {
            ResidentialPropertyDto residential = PropertyDtoMapper.mapToResidentialPropertyDto(propertyDto);
            validatePropertyDto(residential);
            return PropertyMapper.mapDtoToEntity(residential, Property.class);
        } else if (propertyDto.getPropertyCategory().equalsIgnoreCase("COMMERCIAL")) {
                CommercialPropertyDto commercial = PropertyDtoMapper.mapToCommercialPropertyDto(propertyDto);
                validatePropertyDto(commercial);
                return PropertyMapper.mapDtoToEntity(commercial, Property.class);
        } else {
            throw new ApiException("ERR_INVALID_PROPERTY_CATEGORY",
                    "Invalid Property Category: " + propertyDto.getPropertyCategory(), HttpStatus.BAD_REQUEST);
        }
    }

    private static <T> void validatePropertyDto(T dto) throws ApiException {
        Set<ConstraintViolation<T>> violations = CommonValidator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.append(violation.getMessage()).append("\n");
            }
            throw new ApiException("VALIDATION_ERROR", errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private GrantedAuthority getAuthorityOfUser(String userId) {
        return userService.getUser(userId).getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No authority found for user"));
    }


}
