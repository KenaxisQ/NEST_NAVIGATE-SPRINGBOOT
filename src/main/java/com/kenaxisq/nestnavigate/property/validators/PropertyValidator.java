package com.kenaxisq.nestnavigate.property.validators;

import com.kenaxisq.nestnavigate.property.dto.CommercialPropertyDto;
import com.kenaxisq.nestnavigate.property.dto.LandDto;
import com.kenaxisq.nestnavigate.property.dto.PGDto;
import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.utils.property.*;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class PropertyValidator {

    public static List<String> validateResidentialPropertyDto(ResidentialPropertyDto dto) {
        List<String> missingFields = new ArrayList<>();
        if (!StringUtils.hasText(dto.getTitle())) { missingFields.add("title"); }
        if (!StringUtils.hasText(dto.getType())) { missingFields.add("type"); }
        if (dto.getPropertyCategory() == null) { missingFields.add("propertyCategory"); }
        if (dto.getFacing() == null) { missingFields.add("facing"); }
        if (!StringUtils.hasText(String.valueOf(dto.getPropertyListingFor()))) { missingFields.add("propertyListingFor"); }
        if (!StringUtils.hasText(dto.getProjectName())) { missingFields.add("projectName"); }
//        if (dto.getFurnitureStatus() == null) { missingFields.add("furnitureStatus"); }
        if (!StringUtils.hasText(dto.getFurnitureStatusDescription())) { missingFields.add("furnitureStatusDescription"); }
        if (!StringUtils.hasText(dto.getDescription())) { missingFields.add("description"); }
        if (dto.getSuperBuiltUpArea() == null) { missingFields.add("superBuiltUpArea"); }
        if (dto.getCarpetArea() == null) { missingFields.add("carpetArea"); }
        if (dto.getPrice() == null) { missingFields.add("price"); }
        if (dto.getAdvance() == null) { missingFields.add("advance"); }
        if (dto.getLength() == null) { missingFields.add("length"); }
        if (dto.getWidth() == null) { missingFields.add("width"); }
        if (dto.getPoojaRoom() == null) { missingFields.add("poojaRoom"); }
        if (dto.getNoOfBedrooms() == null) { missingFields.add("noOfBedrooms"); }
        if (dto.getNoOfBathrooms() == null) { missingFields.add("noOfBathrooms"); }
        if (dto.getNoOfRooms() == null) { missingFields.add("noOfRooms"); }
        if (dto.getNoOfBalconies() == null) { missingFields.add("noOfBalconies"); }
        if (dto.getIsNegotiable() == null) { missingFields.add("isNegotiable"); }
        if (!StringUtils.hasText(dto.getPrimaryContact())) { missingFields.add("primaryContact"); }
//        if (!StringUtils.hasText(dto.getSecondaryContact())) { missingFields.add("secondaryContact"); }
        if (!StringUtils.hasText(dto.getMandal())) { missingFields.add("mandal"); }
        if (!StringUtils.hasText(dto.getVillage())) { missingFields.add("village"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("zip"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("address"); }
        if (!StringUtils.hasText(dto.getMedia())) { missingFields.add("media"); }
        if (dto.getMoveInDate() == null) { missingFields.add("moveInDate"); }

        // Check for valid enum values
        if (dto.getFacing() != null && !isValidEnumValue(Directions.class, dto.getFacing().name())) {
            missingFields.add("facing (invalid value)");
        }
        if (dto.getFurnitureStatus() != null && !isValidEnumValue(Furniture.class, dto.getFurnitureStatus().name())) {
            missingFields.add("furnitureStatus (invalid value)");
        }
        if (dto.getPropertyCategory() != null && !isValidEnumValue(PropertyCategory.class, dto.getPropertyCategory().name())) {
            missingFields.add("propertyCategory (invalid value)");
        }
        if (dto.getStatus() != null && !isValidEnumValue(PropertyStatus.class, dto.getStatus().name())) {
            missingFields.add("status (invalid value)");
        }
        if (dto.getPropertyListingFor()!= null && !isValidEnumValue(PropertyListingType.class, dto.getPropertyListingFor().name())&& dto.getPropertyListingFor() != PropertyListingType.LEASE) {
            missingFields.add("Property Listed For (invalid value)");
        }
        return missingFields;
    }
    public static List<String> validateCommercialPropertyDto(CommercialPropertyDto dto) {
        List<String> missingFields = new ArrayList<>();
        if (!StringUtils.hasText(dto.getTitle())) { missingFields.add("title"); }
        if (!StringUtils.hasText(dto.getType())) { missingFields.add("type"); }
        if (dto.getPropertyCategory() == null) { missingFields.add("propertyCategory"); }
        if (dto.getFacing() == null) { missingFields.add("facing"); }
        if (!StringUtils.hasText(String.valueOf(dto.getPropertyListingFor()))) { missingFields.add("propertyListingFor"); }
        if (!StringUtils.hasText(dto.getProjectName())) { missingFields.add("projectName"); }
        if (dto.getFurnitureStatus() == null) { missingFields.add("furnitureStatus"); }
//        if (!StringUtils.hasText(dto.getFurnitureStatusDescription())) { missingFields.add("furnitureStatusDescription"); }
        if (!StringUtils.hasText(dto.getDescription())) { missingFields.add("description"); }
        if (dto.getSuperBuiltUpArea() == null) { missingFields.add("superBuiltUpArea"); }
        if (dto.getCarpetArea() == null) { missingFields.add("carpetArea"); }
        if (dto.getPrice() == null) { missingFields.add("price"); }
        if (dto.getAdvance() == null) { missingFields.add("advance"); }
        if (dto.getLength() == null) { missingFields.add("length"); }
        if (dto.getWidth() == null) { missingFields.add("width"); }
        if (dto.getIsNegotiable() == null) { missingFields.add("isNegotiable"); }
        if (!StringUtils.hasText(dto.getPrimaryContact())) { missingFields.add("primaryContact"); }
//        if (!StringUtils.hasText(dto.getSecondaryContact())) { missingFields.add("secondaryContact"); }
        if (!StringUtils.hasText(dto.getMandal())) { missingFields.add("mandal"); }
        if (!StringUtils.hasText(dto.getVillage())) { missingFields.add("village"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("zip"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("address"); }
        if (!StringUtils.hasText(dto.getMedia())) { missingFields.add("media"); }
        if (dto.getMoveInDate() == null) { missingFields.add("moveInDate"); }
        // Check for valid enum values
        if (dto.getFacing() != null && !isValidEnumValue(Directions.class, dto.getFacing().name())) {
            missingFields.add("facing (invalid value)");
        }
        if (dto.getFurnitureStatus() != null && !isValidEnumValue(Furniture.class, dto.getFurnitureStatus().name())) {
            missingFields.add("furnitureStatus (invalid value)");
        }
        if (dto.getPropertyCategory() != null && !isValidEnumValue(PropertyCategory.class, dto.getPropertyCategory().name())) {
            missingFields.add("propertyCategory (invalid value)");
        }
        if (dto.getStatus() != null && !isValidEnumValue(PropertyStatus.class, dto.getStatus().name())) {
            missingFields.add("status (invalid value)");
        }
        if (dto.getPropertyListingFor()!= null && !isValidEnumValue(PropertyListingType.class, dto.getPropertyListingFor().name())&& dto.getPropertyListingFor() != PropertyListingType.RENT) {
            missingFields.add("Property Listed For (invalid value)");
        }
        return missingFields;
    }
    public static List<String> validateLandDto(LandDto dto) {
        List<String> missingFields = new ArrayList<>();
        if (!StringUtils.hasText(dto.getTitle())) { missingFields.add("title"); }
        if (!StringUtils.hasText(dto.getType())) { missingFields.add("type"); }
        if (dto.getPropertyCategory() == null) { missingFields.add("propertyCategory"); }
        if (dto.getFacing() == null) { missingFields.add("facing"); }
        if (!StringUtils.hasText(String.valueOf(dto.getPropertyListingFor()))) { missingFields.add("propertyListingFor"); }
        if (!StringUtils.hasText(dto.getProjectName())) { missingFields.add("projectName"); }
        if (!StringUtils.hasText(dto.getDescription())) { missingFields.add("description"); }
        if (dto.getSuperBuiltUpArea() == null) { missingFields.add("superBuiltUpArea"); }
        if (dto.getPrice() == null) { missingFields.add("price"); }
        if (dto.getAdvance() == null) { missingFields.add("advance"); }
        if (dto.getLength() == null) { missingFields.add("length"); }
        if (dto.getWidth() == null) { missingFields.add("width"); }
        if (dto.getIsNegotiable() == null) { missingFields.add("isNegotiable"); }
        if (!StringUtils.hasText(dto.getPrimaryContact())) { missingFields.add("primaryContact"); }
//        if (!StringUtils.hasText(dto.getSecondaryContact())) { missingFields.add("secondaryContact"); }
        if (!StringUtils.hasText(dto.getMandal())) { missingFields.add("mandal"); }
        if (!StringUtils.hasText(dto.getVillage())) { missingFields.add("village"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("zip"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("address"); }
        if (!StringUtils.hasText(dto.getMedia())) { missingFields.add("media"); }
        // Check for valid enum values
        if (dto.getFacing() != null && !isValidEnumValue(Directions.class, dto.getFacing().name())) {
            missingFields.add("facing (invalid value)");
        }
        if (dto.getPropertyCategory() != null && !isValidEnumValue(PropertyCategory.class, dto.getPropertyCategory().name())) {
                missingFields.add("propertyCategory (invalid value)");
            }
        if (dto.getStatus() != null && !isValidEnumValue(PropertyStatus.class, dto.getStatus().name())) {
            missingFields.add("status (invalid value)");
        }
        if (dto.getPropertyListingFor()!= null && !isValidEnumValue(PropertyListingType.class, dto.getPropertyListingFor().name())&& dto.getPropertyListingFor() != PropertyListingType.RENT) {
            missingFields.add("Property Listed For (invalid value)");
        }
        return missingFields;
    }
    public static List<String> validatePgDto(PGDto dto) {
        List<String> missingFields = new ArrayList<>();
        if (!StringUtils.hasText(dto.getTitle())) { missingFields.add("title"); }
        if (!StringUtils.hasText(dto.getType())) { missingFields.add("type"); }
        if (dto.getPropertyCategory() == null) { missingFields.add("propertyCategory"); }
        if (!StringUtils.hasText(String.valueOf(dto.getPropertyListingFor()))) { missingFields.add("propertyListingFor"); }
        if (!StringUtils.hasText(dto.getProjectName())) { missingFields.add("projectName"); }
        if (!StringUtils.hasText(dto.getDescription())) { missingFields.add("description"); }
        if (dto.getPrice() == null) { missingFields.add("price"); }
        if (dto.getAdvance() == null) { missingFields.add("advance"); }
        if (dto.getIsNegotiable() == null) { missingFields.add("isNegotiable"); }
        if (!StringUtils.hasText(dto.getPrimaryContact())) { missingFields.add("primaryContact"); }
//        if (!StringUtils.hasText(dto.getSecondaryContact())) { missingFields.add("secondaryContact"); }
        if (!StringUtils.hasText(dto.getMandal())) { missingFields.add("mandal"); }
        if (!StringUtils.hasText(dto.getVillage())) { missingFields.add("village"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("zip"); }
        if (!StringUtils.hasText(dto.getZip())) { missingFields.add("address"); }
        if (!StringUtils.hasText(dto.getMedia())) { missingFields.add("media"); }
        // Check for valid enum values
        if (dto.getPropertyCategory() != null && !isValidEnumValue(PropertyCategory.class, dto.getPropertyCategory().name())) {
            missingFields.add("propertyCategory (invalid value)");
        }
        if (dto.getStatus() != null && !isValidEnumValue(PropertyStatus.class, dto.getStatus().name())) {
            missingFields.add("status (invalid value)");
        }
        if (dto.getPropertyListingFor()!= null && !isValidEnumValue(PropertyListingType.class, dto.getPropertyListingFor().name())&& dto.getPropertyListingFor() != PropertyListingType.RENT) {
            missingFields.add("Property Listed For (invalid value)");
        }
        return missingFields;
    }


    private static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String value) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}