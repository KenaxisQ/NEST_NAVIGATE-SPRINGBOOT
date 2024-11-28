package com.kenaxisq.nestnavigate.property.service.commercial;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.CommercialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommercialPropertyServiceImpl implements CommercialPropertyService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertyService propertyService;
    private static final Logger logger = LoggerFactory.getLogger(CommercialPropertyServiceImpl.class);
    @Autowired
    public CommercialPropertyServiceImpl(PropertyRepository propertyRepository,
                                         UserService userService,
                                         PropertyService propertyService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.propertyService = propertyService;

    }

    @Override
    public Property postCommercialProperty(CommercialPropertyDto commercialPropertyDto , String userId) {
        try {
//                Optional user = userRepository.findById(userId).get().(getProperties_listed()<1).orElseThrow()
            User user = userService.getUser(userId);
            if (user.getProperties_listed() < 1) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            List<String> missingFields = PropertyValidator.validateCommercialPropertyDto(commercialPropertyDto);
            if (!missingFields.isEmpty()) {
                logger.error("Missing required fields: " + String.join(", ", missingFields));
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            Property property = mapDtoToEntity(commercialPropertyDto);

            property.setOwner(user);
            property = propertyRepository.save(property);
            userService.updatePropertyListingLimit(userId, user.getProperties_listing_limit() - 1);
            // Save the property entity to the database
            return property;
        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new ApiException("ERR_PRPTY_LSTNG",ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Property updateCommercialProperty(CommercialPropertyDto commercialPropertyDto ,String userId, String propertyId) {
        try {
            // Check if the property exists
            Property existingProperty = propertyService.getPropertyById(propertyId);

            // Verify if the user is valid
            User user = userService.getUser(userId);

            // Optional: Check if the user owns the property
            if (!existingProperty.getOwner().getId().equals(userId) || !user.getRole().equals(UserRole.ADMIN)) {
                throw new ApiException(ErrorCodes.INSUFFICIENT_PERMISSIONS.getCode(),
                        "User does not have permission to update this property",
                        ErrorCodes.INSUFFICIENT_PERMISSIONS.getHttpStatus());
            }

            // Update only the fields that are present in the DTO
            if (commercialPropertyDto.getTitle() != null)
                existingProperty.setTitle(commercialPropertyDto.getTitle());
            if (commercialPropertyDto.getType() != null) existingProperty.setType(commercialPropertyDto.getType());
            if (commercialPropertyDto.getPropertyCategory() != null)
                existingProperty.setPropertyCategory(commercialPropertyDto.getPropertyCategory().name());
            if (commercialPropertyDto.getFacing() != null)
                existingProperty.setFacing(commercialPropertyDto.getFacing().name());
            if (commercialPropertyDto.getPropertyListingFor() != null)
                existingProperty.setPropertyListingFor(commercialPropertyDto.getPropertyListingFor().name());
            if (commercialPropertyDto.getProjectName() != null)
                existingProperty.setProjectName(commercialPropertyDto.getProjectName());
            if (commercialPropertyDto.getFurnitureStatus() != null)
                existingProperty.setFurnitureStatus(commercialPropertyDto.getFurnitureStatus().name());
            if (commercialPropertyDto.getFurnitureStatusDescription() != null)
                existingProperty.setFurnitureStatusDescription(commercialPropertyDto.getFurnitureStatusDescription());
            if (commercialPropertyDto.getDescription() != null)
                existingProperty.setDescription(commercialPropertyDto.getDescription());
            if (commercialPropertyDto.getSuperBuiltUpArea() != null)
                existingProperty.setSuper_builtup_area(commercialPropertyDto.getSuperBuiltUpArea());
            if (commercialPropertyDto.getCarpetArea() != null)
                existingProperty.setCarpet_area(commercialPropertyDto.getCarpetArea());
            if (commercialPropertyDto.getPrice() != null)
                existingProperty.setPrice(commercialPropertyDto.getPrice());
            if (commercialPropertyDto.getAdvance() != null)
                existingProperty.setAdvance(commercialPropertyDto.getAdvance());
            if (commercialPropertyDto.getLength() != null)
                existingProperty.setLength(commercialPropertyDto.getLength());
            if (commercialPropertyDto.getWidth() != null)
                existingProperty.setWidth(commercialPropertyDto.getWidth());
            if (commercialPropertyDto.getIsNegotiable() != null)
                existingProperty.setIsNegotiable(commercialPropertyDto.getIsNegotiable());
            if (commercialPropertyDto.getPrimaryContact() != null)
                existingProperty.setPrimaryContact(commercialPropertyDto.getPrimaryContact());
            if (commercialPropertyDto.getSecondaryContact() != null)
                existingProperty.setSecondaryContact(commercialPropertyDto.getSecondaryContact());
            if (commercialPropertyDto.getMandal() != null)
                existingProperty.setMandal(commercialPropertyDto.getMandal());
            if (commercialPropertyDto.getVillage() != null)
                existingProperty.setVillage(commercialPropertyDto.getVillage());
            if (commercialPropertyDto.getZip() != null) existingProperty.setZip(commercialPropertyDto.getZip());
            if (commercialPropertyDto.getAddress() != null) existingProperty.setAddress(commercialPropertyDto.getAddress());
            if (commercialPropertyDto.getMedia() != null)
                existingProperty.setMedia(commercialPropertyDto.getMedia());
            if (commercialPropertyDto.getMoveInDate() != null)
                existingProperty.setMoveInDate(commercialPropertyDto.getMoveInDate());

            // Set the updated date
            existingProperty.setUpdatedDate(LocalDateTime.now());

            // Save the updated property entity to the database
            return propertyRepository.save(existingProperty);
        }
        catch (ApiException ex){
            throw ex;
        } catch (Exception ex){
            throw new ApiException("ERR_PRPTY_LSTNG",ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public Property mapDtoToEntity(CommercialPropertyDto dto) {
        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setType(dto.getType());
        property.setPropertyCategory(dto.getPropertyCategory().name());
        property.setFacing(dto.getFacing().name());
        property.setPropertyListingFor(dto.getPropertyListingFor().name());
        property.setProjectName(dto.getProjectName());
        property.setFurnitureStatus(dto.getFurnitureStatus().name());
        if( StringUtils.hasText(dto.getFurnitureStatusDescription()))property.setFurnitureStatusDescription(dto.getFurnitureStatusDescription());
        property.setDescription(dto.getDescription());
        property.setSuper_builtup_area(dto.getSuperBuiltUpArea());
        property.setCarpet_area(dto.getCarpetArea());
        property.setPrice(dto.getPrice());
        property.setAdvance(dto.getAdvance());
        property.setLength(dto.getLength());
        property.setWidth(dto.getWidth());
        property.setIsNegotiable(dto.getIsNegotiable());
        property.setPrimaryContact(dto.getPrimaryContact());
        if (StringUtils.hasText(dto.getSecondaryContact())) property.setSecondaryContact(dto.getSecondaryContact());
        property.setMandal(dto.getMandal());
        property.setVillage(dto.getVillage());
        property.setZip(dto.getZip());
        property.setMedia(dto.getMedia());
        property.setAddress(dto.getAddress());
        property.setMoveInDate(dto.getMoveInDate());
        return property;
    }
}
