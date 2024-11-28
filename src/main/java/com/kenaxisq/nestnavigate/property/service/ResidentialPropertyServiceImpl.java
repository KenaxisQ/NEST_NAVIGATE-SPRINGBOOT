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

            // Set the created and updated dates
            property.setListedDate(LocalDateTime.now());
            property.setUpdatedDate(LocalDateTime.now());
            property.setExpiryDate(property.getListedDate().plus(30, ChronoUnit.DAYS));

            // Set the Owner
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(),
                            "Owner with ID " + userId + " not found.",
                            ErrorCodes.USER_NOT_FOUND.getHttpStatus())
            );
            property.setOwner(user);

            // Save the property entity to the database
            return propertyRepository.save(property);
        }

        @Override
        public Property putResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId) {
            List<String> missingFields = PropertyValidator.validateResidentialPropertyDto(residentialPropertyDto);
            if (!missingFields.isEmpty()) {
                throw new ApiException(ErrorCodes.MISSING_REQUIRED_FIELD.getCode(),
                        "Missing required fields: " + String.join(", ", missingFields),
                        ErrorCodes.MISSING_REQUIRED_FIELD.getHttpStatus());
            }
            Property property = mapDtoToEntity(residentialPropertyDto);
            property.setUpdatedDate(LocalDateTime.now());
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(),
                            "Owner with ID " + userId + " not found.",
                            ErrorCodes.USER_NOT_FOUND.getHttpStatus())
            );
            return propertyRepository.save(property);
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

