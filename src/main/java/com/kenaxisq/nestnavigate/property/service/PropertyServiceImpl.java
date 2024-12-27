package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.*;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.filter.dto.PropertyFilterDto;
import com.kenaxisq.nestnavigate.property.filter.service.PropertySpecification;
import com.kenaxisq.nestnavigate.property.mapper.PropertyDtoMapper;
import com.kenaxisq.nestnavigate.property.mapper.PropertyMapper;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.validators.CommonValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.property.PropertyCategory;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertySpecification propertySpecification;
    private static final Logger logger = LoggerFactory.getLogger(Property.class);
    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               UserService userService, PropertySpecification propertySpecification) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.propertySpecification = propertySpecification;
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

    @Transactional(readOnly = true)
    public List<Property> searchProperties(PropertyFilterDto filterDto) {
        return propertyRepository.findAll(propertySpecification.getPropertiesWithFilters(filterDto));
    }

    @Override
    public Property updateProperty(Property property) {
        try {
            if(property.getId()==null || property.getId().isEmpty())
            {
                throw new ApiException("ERR_PROPERTY_UPDATE",
                        "Property Id is required!!", HttpStatus.BAD_REQUEST);
            }
            Optional<Property> propertyOptional = propertyRepository.findById(property.getId());
            if (!propertyOptional.isPresent()) {
                throw new ApiException("ERR_PROPERTY_NOT_FOUND",
                        "No Property with Id: " + property.getId() + " found!!", HttpStatus.NOT_FOUND);
            }
            Property existingProperty = propertyOptional.get();
            boolean categoryChanged = !existingProperty.getPropertyCategory().equals(property.getPropertyCategory());
            existingProperty = updateNonNullFields(existingProperty, property);
            if (categoryChanged) {
                validatePropertyDto(PropertyDtoMapper.mapPropertyToDto(property));
                existingProperty.setPropertyCategory(property.getPropertyCategory());
            }
            existingProperty.setUpdatedDate(LocalDateTime.now());
            return propertyRepository.save(existingProperty);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("ERR_PROPERTY_UPDATE",
                    "Error updating property: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Property postProperty(AggregatePropertyDto propertyDto, String userId) {
        logger.info("Received Property Data: " + propertyDto.toString());
        try {
            userService.getUser(userId);
            if(propertyRepository.findPropertyById(propertyDto.getId())!=null)
                throw new ApiException("PROPERTY_ID_ALREADY_EXISTS","PropertyID: "+propertyDto.getId()+" already exists, try different id..",HttpStatus.BAD_REQUEST);
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
            User owner = userService.getUser(userId);
            property.setListedby(owner.getRole().toString());
            property.setOwner(owner);
            property.initializeApprovalStatus(owner.getRole().toString());
            logger.info("Saving property: " + property.toString());
            Property savedProperty = propertyRepository.save(property);
            owner.setProperties_listed(owner.getProperties_listed()+1);
            owner.setProperties_listing_limit(owner.getProperties_listing_limit()-1);
            userService.updateUser(owner);
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

    private Property updateNonNullFields(Property existingProperty, Property newPropertyData) {
        if (newPropertyData.getTitle() != null) existingProperty.setTitle(newPropertyData.getTitle());
        if (newPropertyData.getType() != null) existingProperty.setType(newPropertyData.getType());
        if (newPropertyData.getFacing() != null) existingProperty.setFacing(newPropertyData.getFacing());
        if (newPropertyData.getPropertyListingFor() != null) existingProperty.setPropertyListingFor(newPropertyData.getPropertyListingFor());
        if (newPropertyData.getProjectName() != null) existingProperty.setProjectName(newPropertyData.getProjectName());
        if (newPropertyData.getFurnitureStatus() != null) existingProperty.setFurnitureStatus(newPropertyData.getFurnitureStatus());
        if (newPropertyData.getFurnitureStatusDescription() != null) existingProperty.setFurnitureStatusDescription(newPropertyData.getFurnitureStatusDescription());
        if (newPropertyData.getDescription() != null) existingProperty.setDescription(newPropertyData.getDescription());
        if (newPropertyData.getSuperBuiltupArea() != null) existingProperty.setSuperBuiltupArea(newPropertyData.getSuperBuiltupArea());
        if (newPropertyData.getCarpetArea() != null) existingProperty.setCarpetArea(newPropertyData.getCarpetArea());
        if (newPropertyData.getPrice() != null) existingProperty.setPrice(newPropertyData.getPrice());
        if (newPropertyData.getAdvance() != null) existingProperty.setAdvance(newPropertyData.getAdvance());
        if (newPropertyData.getLength() != null) existingProperty.setLength(newPropertyData.getLength());
        if (newPropertyData.getWidth() != null) existingProperty.setWidth(newPropertyData.getWidth());
        if (newPropertyData.getPoojaRoom() != null) existingProperty.setPoojaRoom(newPropertyData.getPoojaRoom());
        if (newPropertyData.getNoOfBedrooms() != null) existingProperty.setNoOfBedrooms(newPropertyData.getNoOfBedrooms());
        if (newPropertyData.getNoOfBathrooms() != null) existingProperty.setNoOfBathrooms(newPropertyData.getNoOfBathrooms());
        if (newPropertyData.getNoOfRooms() != null) existingProperty.setNoOfRooms(newPropertyData.getNoOfRooms());
        if (newPropertyData.getNoOfBalconies() != null) existingProperty.setNoOfBalconies(newPropertyData.getNoOfBalconies());
        if (newPropertyData.getIsNegotiable() != null) existingProperty.setIsNegotiable(newPropertyData.getIsNegotiable());
        if (newPropertyData.getStatus() != null) existingProperty.setStatus(newPropertyData.getStatus());
        if (newPropertyData.getIsFeatured() != null) existingProperty.setIsFeatured(newPropertyData.getIsFeatured());
        if (newPropertyData.getPrimaryContact() != null) existingProperty.setPrimaryContact(newPropertyData.getPrimaryContact());
        if (newPropertyData.getSecondaryContact() != null) existingProperty.setSecondaryContact(newPropertyData.getSecondaryContact());
        if (newPropertyData.getState() != null) existingProperty.setState(newPropertyData.getState());
        if (newPropertyData.getCountry() != null) existingProperty.setCountry(newPropertyData.getCountry());
        if (newPropertyData.getRevenueDivision() != null) existingProperty.setRevenueDivision(newPropertyData.getRevenueDivision());
        if (newPropertyData.getMandal() != null) existingProperty.setMandal(newPropertyData.getMandal());
        if (newPropertyData.getVillage() != null) existingProperty.setVillage(newPropertyData.getVillage());
        if (newPropertyData.getZip() != null) existingProperty.setZip(newPropertyData.getZip());
        if (newPropertyData.getAddress() != null) existingProperty.setAddress(newPropertyData.getAddress());
        if (newPropertyData.getLongitude() != null) existingProperty.setLongitude(newPropertyData.getLongitude());
        if (newPropertyData.getLatitude() != null) existingProperty.setLatitude(newPropertyData.getLatitude());
        if (newPropertyData.getMedia() != null) existingProperty.setMedia(newPropertyData.getMedia());
        if (newPropertyData.getMoveInDate() != null) existingProperty.setMoveInDate(newPropertyData.getMoveInDate());
        if (newPropertyData.getAmenities() != null) existingProperty.setAmenities(newPropertyData.getAmenities());
        if(newPropertyData.getLikes() != null) existingProperty.setLikes(newPropertyData.getLikes());
        if(newPropertyData.getViews() != null) existingProperty.setViews(newPropertyData.getViews());
        if(newPropertyData.getPropertyApprovalStatus()!=null) existingProperty.setPropertyApprovalStatus(newPropertyData.getPropertyApprovalStatus());
        return existingProperty;
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
