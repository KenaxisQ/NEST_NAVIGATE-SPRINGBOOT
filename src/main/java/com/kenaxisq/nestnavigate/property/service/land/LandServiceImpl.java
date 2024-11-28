package com.kenaxisq.nestnavigate.property.service.land;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.LandDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LandServiceImpl implements LandService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertyService propertyService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LandServiceImpl.class);
    public LandServiceImpl(PropertyRepository propertyRepository,
                           UserService userService,
                           PropertyService propertyService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
       this.propertyService = propertyService;
    }


    @Override
    public Property postLandProperty(LandDto landDto, String userId) {
        try {
//                Optional user = userRepository.findById(userId).get().(getProperties_listed()<1).orElseThrow()
            User user = userService.getUser(userId);
            if (user.getProperties_listed() < 1) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            List<String> missingFields = PropertyValidator.validateLandDto(landDto);
            if (!missingFields.isEmpty()) {
                logger.error("Missing required fields: " + String.join(", ", missingFields));
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            Property property = mapDtoToEntity(landDto);

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
    public Property updateLandProperty(LandDto landDto, String userId, String propertyId) {
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
            if (landDto.getTitle() != null)
                existingProperty.setTitle(landDto.getTitle());
            if (landDto.getType() != null) existingProperty.setType(landDto.getType());
            if (landDto.getPropertyCategory() != null)
                existingProperty.setPropertyCategory(landDto.getPropertyCategory().name());
            if (landDto.getFacing() != null)
                existingProperty.setFacing(landDto.getFacing().name());
            if (landDto.getPropertyListingFor() != null)
                existingProperty.setPropertyListingFor(landDto.getPropertyListingFor().name());
            if (landDto.getProjectName() != null)
                existingProperty.setProjectName(landDto.getProjectName());
            if (landDto.getDescription() != null)
                existingProperty.setDescription(landDto.getDescription());
            if (landDto.getSuperBuiltUpArea() != null)
                existingProperty.setSuper_builtup_area(landDto.getSuperBuiltUpArea());
            if (landDto.getPrice() != null)
                existingProperty.setPrice(landDto.getPrice());
            if (landDto.getAdvance() != null)
                existingProperty.setAdvance(landDto.getAdvance());
            if (landDto.getLength() != null)
                existingProperty.setLength(landDto.getLength());
            if (landDto.getWidth() != null)
                existingProperty.setWidth(landDto.getWidth());
            if (landDto.getIsNegotiable() != null)
                existingProperty.setIsNegotiable(landDto.getIsNegotiable());
            if (landDto.getPrimaryContact() != null)
                existingProperty.setPrimaryContact(landDto.getPrimaryContact());
            if (landDto.getSecondaryContact() != null)
                existingProperty.setSecondaryContact(landDto.getSecondaryContact());
            if (landDto.getMandal() != null)
                existingProperty.setMandal(landDto.getMandal());
            if (landDto.getVillage() != null)
                existingProperty.setVillage(landDto.getVillage());
            if (landDto.getZip() != null) existingProperty.setZip(landDto.getZip());
            if (landDto.getMedia() != null)
                existingProperty.setMedia(landDto.getMedia());
            if (landDto.getAddress() != null)
                existingProperty.setAddress(landDto.getAddress());
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

    public Property mapDtoToEntity(LandDto dto) {
        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setType(dto.getType());
        property.setPropertyCategory(dto.getPropertyCategory().name());
        property.setFacing(dto.getFacing().name());
        property.setPropertyListingFor(dto.getPropertyListingFor().name());
        property.setProjectName(dto.getProjectName());
        property.setDescription(dto.getDescription());
        property.setSuper_builtup_area(dto.getSuperBuiltUpArea());
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
        return property;
    }
}
