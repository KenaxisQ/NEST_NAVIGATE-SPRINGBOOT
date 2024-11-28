package com.kenaxisq.nestnavigate.property.service.residential;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.UserRole;
import com.kenaxisq.nestnavigate.utils.property.Directions;
import com.kenaxisq.nestnavigate.utils.property.Furniture;
import com.kenaxisq.nestnavigate.utils.property.PropertyListingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
    public class ResidentialPropertyServiceImpl implements ResidentialPropertyService {
        private final PropertyRepository propertyRepository;
        private final UserService userService;
        private final PropertyService propertyService;
        private static final Logger logger = LoggerFactory.getLogger(ResidentialPropertyServiceImpl.class);
        @Autowired
        public ResidentialPropertyServiceImpl(PropertyRepository propertyRepository,
                                              UserService userService,
                                              PropertyService propertyService) {
            this.propertyRepository = propertyRepository;
            this.propertyService = propertyService;
            this.userService = userService;
        }

        @Override
        public Property postResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId) {

            try {
//                Optional user = userRepository.findById(userId).get().(getProperties_listed()<1).orElseThrow()
                User user = userService.getUser(userId);
                if (user.getProperties_listed() < 1) {
                    throw new ApiException(ErrorCodes.PROPERTY_LISTING_LIMIT_EXCEEDED);
                }
                List<String> missingFields = PropertyValidator.validateResidentialPropertyDto(residentialPropertyDto);
                if (!missingFields.isEmpty()) {
                    logger.error("Missing required fields: " + String.join(", ", missingFields));
                    throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                            "Missing required fields: " + String.join(", ", missingFields),
                            ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
                }
                Property property = mapResidentialPropertyDtoToProperty(residentialPropertyDto);

                property.setOwner(user);
                property = propertyRepository.save(property);
                userService.updatePropertyListingLimit(userId, user.getProperties_listing_limit() - 1);
                userService.updatePropertyListed(userId,user.getProperties_listed()+1);
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
        public Property updateResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId, String propertyId) {
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
               if (residentialPropertyDto.getTitle() != null)
                   existingProperty.setTitle(residentialPropertyDto.getTitle());
               if (residentialPropertyDto.getType() != null) existingProperty.setType(residentialPropertyDto.getType());
               if (residentialPropertyDto.getPropertyCategory() != null)
                   existingProperty.setPropertyCategory(residentialPropertyDto.getPropertyCategory().name());
               if (residentialPropertyDto.getFacing() != null)
                   existingProperty.setFacing(residentialPropertyDto.getFacing().name());
               if (residentialPropertyDto.getPropertyListingFor() != null)
                   existingProperty.setPropertyListingFor(residentialPropertyDto.getPropertyListingFor().name());
               if (residentialPropertyDto.getProjectName() != null)
                   existingProperty.setProjectName(residentialPropertyDto.getProjectName());
               if (residentialPropertyDto.getFurnitureStatus() != null)
                   existingProperty.setFurnitureStatus(residentialPropertyDto.getFurnitureStatus().name());
               if (residentialPropertyDto.getFurnitureStatusDescription() != null)
                   existingProperty.setFurnitureStatusDescription(residentialPropertyDto.getFurnitureStatusDescription());
               if (residentialPropertyDto.getDescription() != null)
                   existingProperty.setDescription(residentialPropertyDto.getDescription());
               if (residentialPropertyDto.getSuperBuiltUpArea() != null)
                   existingProperty.setSuper_builtup_area(residentialPropertyDto.getSuperBuiltUpArea());
               if (residentialPropertyDto.getCarpetArea() != null)
                   existingProperty.setCarpet_area(residentialPropertyDto.getCarpetArea());
               if (residentialPropertyDto.getPrice() != null)
                   existingProperty.setPrice(residentialPropertyDto.getPrice());
               if (residentialPropertyDto.getAdvance() != null)
                   existingProperty.setAdvance(residentialPropertyDto.getAdvance());
               if (residentialPropertyDto.getLength() != null)
                   existingProperty.setLength(residentialPropertyDto.getLength());
               if (residentialPropertyDto.getWidth() != null)
                   existingProperty.setWidth(residentialPropertyDto.getWidth());
               if (residentialPropertyDto.getPoojaRoom() != null)
                   existingProperty.setPoojaRoom(residentialPropertyDto.getPoojaRoom());
               if (residentialPropertyDto.getNoOfBedrooms() != null)
                   existingProperty.setNoOfBedrooms(residentialPropertyDto.getNoOfBedrooms());
               if (residentialPropertyDto.getNoOfBathrooms() != null)
                   existingProperty.setNoOfBathrooms(residentialPropertyDto.getNoOfBathrooms());
               if (residentialPropertyDto.getNoOfRooms() != null)
                   existingProperty.setNoOfRooms(residentialPropertyDto.getNoOfRooms());
               if (residentialPropertyDto.getNoOfBalconies() != null)
                   existingProperty.setNoOfBalconies(residentialPropertyDto.getNoOfBalconies());
               if (residentialPropertyDto.getIsNegotiable() != null)
                   existingProperty.setIsNegotiable(residentialPropertyDto.getIsNegotiable());
               if (residentialPropertyDto.getPrimaryContact() != null)
                   existingProperty.setPrimaryContact(residentialPropertyDto.getPrimaryContact());
               if (residentialPropertyDto.getSecondaryContact() != null)
                   existingProperty.setSecondaryContact(residentialPropertyDto.getSecondaryContact());
               if (residentialPropertyDto.getMandal() != null)
                   existingProperty.setMandal(residentialPropertyDto.getMandal());
               if (residentialPropertyDto.getVillage() != null)
                   existingProperty.setVillage(residentialPropertyDto.getVillage());
               if (residentialPropertyDto.getZip() != null) existingProperty.setZip(residentialPropertyDto.getZip());
               if (residentialPropertyDto.getAddress() != null) existingProperty.setAddress(residentialPropertyDto.getAddress());
               if (residentialPropertyDto.getMedia() != null)
                   existingProperty.setMedia(residentialPropertyDto.getMedia());
               if (residentialPropertyDto.getMoveInDate() != null)
                   existingProperty.setMoveInDate(residentialPropertyDto.getMoveInDate());

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
        public Property mapResidentialPropertyDtoToProperty(ResidentialPropertyDto dto) {
            Property property = new Property();
            property.setTitle(dto.getTitle());
            property.setType(dto.getType());
            property.setPropertyCategory(dto.getPropertyCategory().name());
            property.setFacing(dto.getFacing().name());
            property.setPropertyListingFor(dto.getPropertyListingFor().name());
            property.setProjectName(dto.getProjectName());
            property.setFurnitureStatus(dto.getFurnitureStatus().name());
            if(StringUtils.hasText(dto.getFurnitureStatusDescription()))property.setFurnitureStatusDescription(dto.getFurnitureStatusDescription());
            property.setDescription(dto.getDescription());
            property.setSuper_builtup_area(dto.getSuperBuiltUpArea());
            property.setCarpet_area(dto.getCarpetArea());
            property.setPrice(dto.getPrice());
            property.setAdvance(dto.getAdvance());
            property.setLength(dto.getLength());
            property.setWidth(dto.getWidth());
            property.setPoojaRoom(dto.getPoojaRoom());
            property.setNoOfBedrooms(dto.getNoOfBedrooms());
            property.setNoOfBathrooms(dto.getNoOfBathrooms());
            property.setNoOfRooms(dto.getNoOfRooms());
            property.setNoOfBalconies(dto.getNoOfBalconies());
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
        public ResidentialPropertyDto mapPropertyToResidentialPropertyDto(Property property) {
        ResidentialPropertyDto dto = new ResidentialPropertyDto();
        if (property.getTitle() != null) dto.setTitle(property.getTitle());
        if(property.getType() != null) dto.setType(property.getType());
        if (property.getFacing() != null)dto.setFacing(Directions.valueOf(property.getFacing()));
        if (property.getPropertyListingFor() != null)dto.setPropertyListingFor(PropertyListingType.valueOf(property.getPropertyListingFor()));
        dto.setProjectName(property.getProjectName());

        if (property.getFurnitureStatus() != null) {
            dto.setFurnitureStatus(Furniture.valueOf(property.getFurnitureStatus()));
        }

        dto.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
        dto.setDescription(property.getDescription());
        dto.setSuperBuiltUpArea(property.getSuper_builtup_area());
        dto.setCarpetArea(property.getCarpet_area());
        dto.setPrice(property.getPrice());
        dto.setAdvance(property.getAdvance());
        dto.setLength(property.getLength());
        dto.setWidth(property.getWidth());
        dto.setPoojaRoom(property.getPoojaRoom());
        dto.setNoOfBedrooms(property.getNoOfBedrooms());
        dto.setNoOfBathrooms(property.getNoOfBathrooms());
        dto.setNoOfRooms(property.getNoOfRooms());
        dto.setNoOfBalconies(property.getNoOfBalconies());
        dto.setIsNegotiable(property.getIsNegotiable());
        dto.setPrimaryContact(property.getPrimaryContact());
        dto.setSecondaryContact(property.getSecondaryContact());
        dto.setMandal(property.getMandal());
        dto.setVillage(property.getVillage());
        dto.setZip(property.getZip());
        dto.setMedia(property.getMedia());
        dto.setAddress(property.getAddress());
        dto.setMoveInDate(property.getMoveInDate());

        return dto;
    }
    }