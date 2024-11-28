package com.kenaxisq.nestnavigate.property.service.pg;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.LandDto;
import com.kenaxisq.nestnavigate.property.dto.PGDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.service.land.LandServiceImpl;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class PGServiceImpl implements PGService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertyService propertyService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LandServiceImpl.class);
    public PGServiceImpl(PropertyRepository propertyRepository,
                         UserService userService,
                         PropertyService propertyService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @Override
    public Property postPgProperty(PGDto pgDto, String userId) {
        try {
//                Optional user = userRepository.findById(userId).get().(getProperties_listed()<1).orElseThrow()
            User user = userService.getUser(userId);
            if (user.getProperties_listed() < 1) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            List<String> missingFields = PropertyValidator.validatePgDto(pgDto);
            if (!missingFields.isEmpty()) {
                logger.error("Missing required fields: " + String.join(", ", missingFields));
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            Property property = mapDtoToEntity(pgDto);

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
    public Property updatePgProperty(PGDto pgDto, String userId, String propertyId) {
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
            if (pgDto.getTitle() != null)
                existingProperty.setTitle(pgDto.getTitle());
            if (pgDto.getType() != null) existingProperty.setType(pgDto.getType());
            if (pgDto.getPropertyCategory() != null)
                existingProperty.setPropertyCategory(pgDto.getPropertyCategory().name());
            if (pgDto.getProjectName() != null)
                existingProperty.setProjectName(pgDto.getProjectName());
            if (pgDto.getFurnitureStatus() != null)
                existingProperty.setFurnitureStatus(pgDto.getFurnitureStatus().name());
            if (pgDto.getFurnitureStatusDescription() != null)
                existingProperty.setFurnitureStatusDescription(pgDto.getFurnitureStatusDescription());
            if (pgDto.getDescription() != null)
                existingProperty.setDescription(pgDto.getDescription());
            if (pgDto.getPrice() != null)
                existingProperty.setPrice(pgDto.getPrice());
            if (pgDto.getAdvance() != null)
                existingProperty.setAdvance(pgDto.getAdvance());
            if (pgDto.getIsNegotiable() != null)
                existingProperty.setIsNegotiable(pgDto.getIsNegotiable());
            if (pgDto.getPrimaryContact() != null)
                existingProperty.setPrimaryContact(pgDto.getPrimaryContact());
            if (pgDto.getSecondaryContact() != null)
                existingProperty.setSecondaryContact(pgDto.getSecondaryContact());
            if (pgDto.getMandal() != null)
                existingProperty.setMandal(pgDto.getMandal());
            if (pgDto.getVillage() != null)
                existingProperty.setVillage(pgDto.getVillage());
            if (pgDto.getZip() != null) existingProperty.setZip(pgDto.getZip());
            if (pgDto.getAddress() != null) existingProperty.setAddress(pgDto.getAddress());
            if (pgDto.getMedia() != null)
                existingProperty.setMedia(pgDto.getMedia());

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

    public Property mapDtoToEntity(PGDto dto) {
        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setType(dto.getType());
        property.setPropertyCategory(dto.getPropertyCategory().name());
        property.setPropertyListingFor(dto.getPropertyListingFor().name());
        property.setProjectName(dto.getProjectName());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());
        property.setAdvance(dto.getAdvance());
        property.setIsNegotiable(dto.getIsNegotiable());
        property.setPrimaryContact(dto.getPrimaryContact());
        if (StringUtils.hasText(dto.getSecondaryContact())) property.setSecondaryContact(dto.getSecondaryContact());
        property.setMandal(dto.getMandal());
        property.setVillage(dto.getVillage());
        property.setZip(dto.getZip());
        property.setMedia(dto.getMedia());
        property.setAddress(dto.getAddress());
        return property;
    }

}
