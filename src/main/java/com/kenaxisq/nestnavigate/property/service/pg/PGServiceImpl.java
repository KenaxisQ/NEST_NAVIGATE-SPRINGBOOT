package com.kenaxisq.nestnavigate.property.service.pg;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PGServiceImpl implements PGService{

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final PropertyService propertyService;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PGServiceImpl.class);

    @Autowired
    public PGServiceImpl(PropertyRepository propertyRepository,
                         UserService userService,
                         PropertyService propertyService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @Override
    public Property postPgProperty(Property pg, String userId) {
        try {
            User user = userService.getUser(userId);
            if (user.getProperties_listed() < 1) {
                throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
            }
            List<String> missingFields = PropertyValidator.validatePg(pg);
            if (!missingFields.isEmpty()) {
                logger.error("Missing required fields: " + String.join(", ", missingFields));
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            pg.setOwner(user);
            pg = propertyRepository.save(pg);
            userService.updatePropertyListingLimit(userId, user.getProperties_listing_limit() - 1);
            // Save the property entity to the database
            return pg;
        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new ApiException("ERR_PRPTY_LSTNG",ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Property updatePgProperty(Property pg, String userId, String propertyId) {
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
            if (pg.getTitle() != null)
                existingProperty.setTitle(pg.getTitle());
            if (pg.getType() != null) existingProperty.setType(pg.getType());
            if (pg.getPropertyCategory() != null)
                existingProperty.setPropertyCategory(pg.getPropertyCategory());
            if (pg.getProjectName() != null)
                existingProperty.setProjectName(pg.getProjectName());
            if (pg.getFurnitureStatus() != null)
                existingProperty.setFurnitureStatus(pg.getFurnitureStatus());
            if (pg.getFurnitureStatusDescription() != null)
                existingProperty.setFurnitureStatusDescription(pg.getFurnitureStatusDescription());
            if (pg.getDescription() != null)
                existingProperty.setDescription(pg.getDescription());
            if (pg.getPrice() != null)
                existingProperty.setPrice(pg.getPrice());
            if (pg.getAdvance() != null)
                existingProperty.setAdvance(pg.getAdvance());
            if (pg.getIsNegotiable() != null)
                existingProperty.setIsNegotiable(pg.getIsNegotiable());
            if (pg.getPrimaryContact() != null)
                existingProperty.setPrimaryContact(pg.getPrimaryContact());
            if (pg.getSecondaryContact() != null)
                existingProperty.setSecondaryContact(pg.getSecondaryContact());
            if (pg.getMandal() != null)
                existingProperty.setMandal(pg.getMandal());
            if (pg.getVillage() != null)
                existingProperty.setVillage(pg.getVillage());
            if (pg.getZip() != null) existingProperty.setZip(pg.getZip());
            if (pg.getAddress() != null) existingProperty.setAddress(pg.getAddress());
            if (pg.getMedia() != null)
                existingProperty.setMedia(pg.getMedia());

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
