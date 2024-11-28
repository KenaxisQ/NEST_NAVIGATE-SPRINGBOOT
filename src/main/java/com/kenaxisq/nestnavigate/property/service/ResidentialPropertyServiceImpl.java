package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.repository.PropertyRepository;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

    @Service
    public class ResidentialPropertyServiceImpl implements ResidentialPropertyService {
        private final PropertyRepository propertyRepository;
        private final  UserRepository userRepository;

        @Autowired
        public ResidentialPropertyServiceImpl(PropertyRepository propertyRepository, UserRepository userRepository) {
            this.propertyRepository = propertyRepository;
            this.userRepository = userRepository;
        }

        @Override
        public Property postResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId) {
            List<String> missingFields = PropertyValidator.validateResidentialPropertyDto(residentialPropertyDto);
            if (!missingFields.isEmpty()) {
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            Property property = mapDtoToEntity(residentialPropertyDto);
            // Set the Owner
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(),
                            "Owner with ID " + userId + " not found.",
                            ErrorCodes.USER_NOT_FOUND.getHttpStatus()));
            property.setOwner(user);
            // Save the property entity to the database
            return propertyRepository.save(property);
        }
        @Override
        public Property updateResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId, String propertyId) {
            // Check if the property exists
            Property existingProperty = propertyRepository.findById(propertyId).orElseThrow(() ->
                    new ApiException(ErrorCodes.ERR_PROPERTY_NOT_FOUND.getCode(),
                            "Property with ID " + propertyId + " not found.",
                            ErrorCodes.ERR_PROPERTY_NOT_FOUND.getHttpStatus()));

            // Verify if the user is valid
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(),
                            "User with ID " + userId + " not found.",
                            ErrorCodes.USER_NOT_FOUND.getHttpStatus()));

            // Optional: Check if the user owns the property
            if (!existingProperty.getOwner().getId().equals(userId)) {
                throw new ApiException(ErrorCodes.INSUFFICIENT_PERMISSIONS.getCode(),
                        "User does not have permission to update this property",
                        ErrorCodes.INSUFFICIENT_PERMISSIONS.getHttpStatus());
            }

            // Validate potential updates in the DTO
            List<String> missingFields = PropertyValidator.validateResidentialPropertyDto(residentialPropertyDto);
            if (!missingFields.isEmpty()) {
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing or invalid fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }

            // Update only the fields that are present in the DTO
            if (residentialPropertyDto.getTitle() != null) existingProperty.setTitle(residentialPropertyDto.getTitle());
            if (residentialPropertyDto.getType() != null) existingProperty.setType(residentialPropertyDto.getType());
            if (residentialPropertyDto.getPropertyCategory() != null) existingProperty.setPropertyCategory(residentialPropertyDto.getPropertyCategory().name());
            if (residentialPropertyDto.getFacing() != null) existingProperty.setFacing(residentialPropertyDto.getFacing().name());
            if (residentialPropertyDto.getPropertyListingFor() != null) existingProperty.setPropertyListingFor(residentialPropertyDto.getPropertyListingFor().name());
            if (residentialPropertyDto.getProjectName() != null) existingProperty.setProjectName(residentialPropertyDto.getProjectName());
            if (residentialPropertyDto.getFurnitureStatus() != null) existingProperty.setFurnitureStatus(residentialPropertyDto.getFurnitureStatus().name());
            if (residentialPropertyDto.getFurnitureStatusDescription() != null) existingProperty.setFurnitureStatusDescription(residentialPropertyDto.getFurnitureStatusDescription());
            if (residentialPropertyDto.getDescription() != null) existingProperty.setDescription(residentialPropertyDto.getDescription());
            if (residentialPropertyDto.getSuperBuiltUpArea() != null) existingProperty.setSuper_builtup_area(residentialPropertyDto.getSuperBuiltUpArea());
            if (residentialPropertyDto.getCarpetArea() != null) existingProperty.setCarpet_area(residentialPropertyDto.getCarpetArea());
            if (residentialPropertyDto.getPrice() != null) existingProperty.setPrice(residentialPropertyDto.getPrice());
            if (residentialPropertyDto.getAdvance() != null) existingProperty.setAdvance(residentialPropertyDto.getAdvance());
            if (residentialPropertyDto.getLength() != null) existingProperty.setLength(residentialPropertyDto.getLength());
            if (residentialPropertyDto.getWidth() != null) existingProperty.setWidth(residentialPropertyDto.getWidth());
            if (residentialPropertyDto.getPoojaRoom() != null) existingProperty.setPoojaRoom(residentialPropertyDto.getPoojaRoom());
            if (residentialPropertyDto.getNoOfBedrooms() != null) existingProperty.setNoOfBedrooms(residentialPropertyDto.getNoOfBedrooms());
            if (residentialPropertyDto.getNoOfBathrooms() != null) existingProperty.setNoOfBathrooms(residentialPropertyDto.getNoOfBathrooms());
            if (residentialPropertyDto.getNoOfRooms() != null) existingProperty.setNoOfRooms(residentialPropertyDto.getNoOfRooms());
            if (residentialPropertyDto.getNoOfBalconies() != null) existingProperty.setNoOfBalconies(residentialPropertyDto.getNoOfBalconies());
            if (residentialPropertyDto.getIsNegotiable() != null) existingProperty.setIsNegotiable(residentialPropertyDto.getIsNegotiable());
            if (residentialPropertyDto.getPrimaryContact() != null) existingProperty.setPrimaryContact(residentialPropertyDto.getPrimaryContact());
            if (residentialPropertyDto.getSecondaryContact() != null) existingProperty.setSecondaryContact(residentialPropertyDto.getSecondaryContact());
            if (residentialPropertyDto.getMandal() != null) existingProperty.setMandal(residentialPropertyDto.getMandal());
            if (residentialPropertyDto.getVillage() != null) existingProperty.setVillage(residentialPropertyDto.getVillage());
            if (residentialPropertyDto.getZip() != null) existingProperty.setZip(residentialPropertyDto.getZip());
            if (residentialPropertyDto.getMedia() != null) existingProperty.setMedia(residentialPropertyDto.getMedia());
            if (residentialPropertyDto.getMoveInDate() != null) existingProperty.setMoveInDate(residentialPropertyDto.getMoveInDate());

            // Set the updated date
            existingProperty.setUpdatedDate(LocalDateTime.now());

            // Save the updated property entity to the database
            return propertyRepository.save(existingProperty);
        }
        private Property mapDtoToEntity(ResidentialPropertyDto dto) {
            Property property = new Property();
            property.setTitle(dto.getTitle());
            property.setType(dto.getType());
            property.setPropertyCategory(dto.getPropertyCategory().name());
            property.setFacing(dto.getFacing().name());
            property.setPropertyListingFor(dto.getPropertyListingFor().name());
            property.setProjectName(dto.getProjectName());
            property.setFurnitureStatus(dto.getFurnitureStatus().name());
            property.setFurnitureStatusDescription(dto.getFurnitureStatusDescription());
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
            property.setSecondaryContact(dto.getSecondaryContact());
            property.setMandal(dto.getMandal());
            property.setVillage(dto.getVillage());
            property.setZip(dto.getZip());
            property.setMedia(dto.getMedia());
            property.setMoveInDate(dto.getMoveInDate());
            return property;
        }
    }