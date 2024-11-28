package com.kenaxisq.nestnavigate.property.service.land;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public Property postLandProperty(Property land, String userId) {
        try {
            User user = userService.getUser(userId);
            if (user.getProperties_listed() < 1) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            List<String> missingFields = PropertyValidator.validateLand(land);
            if (!missingFields.isEmpty()) {
                logger.error("Missing required fields: " + String.join(", ", missingFields));
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            land.setOwner(user);
            Property property = new Property();
            property = propertyRepository.save(land);
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
    public Property updateLandProperty(Property land, String userId, String propertyId) {
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
            if (land.getTitle() != null)
                existingProperty.setTitle(land.getTitle());
            if (land.getType() != null) existingProperty.setType(land.getType());
            if (land.getPropertyCategory() != null)
                existingProperty.setPropertyCategory(land.getPropertyCategory());
            if (land.getFacing() != null)
                existingProperty.setFacing(land.getFacing());
            if (land.getPropertyListingFor() != null)
                existingProperty.setPropertyListingFor(land.getPropertyListingFor());
            if (land.getProjectName() != null)
                existingProperty.setProjectName(land.getProjectName());
            if (land.getDescription() != null)
                existingProperty.setDescription(land.getDescription());
            if (land.getSuper_builtup_area() != null)
                existingProperty.setSuper_builtup_area(land.getSuper_builtup_area());
            if (land.getPrice() != null)
                existingProperty.setPrice(land.getPrice());
            if (land.getAdvance() != null)
                existingProperty.setAdvance(land.getAdvance());
            if (land.getLength() != null)
                existingProperty.setLength(land.getLength());
            if (land.getWidth() != null)
                existingProperty.setWidth(land.getWidth());
            if (land.getIsNegotiable() != null)
                existingProperty.setIsNegotiable(land.getIsNegotiable());
            if (land.getPrimaryContact() != null)
                existingProperty.setPrimaryContact(land.getPrimaryContact());
            if (land.getSecondaryContact() != null)
                existingProperty.setSecondaryContact(land.getSecondaryContact());
            if (land.getMandal() != null)
                existingProperty.setMandal(land.getMandal());
            if (land.getVillage() != null)
                existingProperty.setVillage(land.getVillage());
            if (land.getZip() != null) existingProperty.setZip(land.getZip());
            if (land.getMedia() != null)
                existingProperty.setMedia(land.getMedia());
            if (land.getAddress() != null)
                existingProperty.setAddress(land.getAddress());
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

}
