package com.kenaxisq.nestnavigate.property.validators;

import java.util.ArrayList;
import java.util.List;

import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.utils.property.Directions;
import com.kenaxisq.nestnavigate.utils.property.Furniture;
import com.kenaxisq.nestnavigate.utils.property.PropertyCategory;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;
import org.springframework.util.StringUtils;

public class PropertyValidator {

    public static List<String> validateResidentialPropertyDto(ResidentialPropertyDto residentialPropertyDto) {
        List<String> missingFields = new ArrayList<>();
        if (!StringUtils.hasText(residentialPropertyDto.getTitle())) { missingFields.add("title"); }
        if (!StringUtils.hasText(residentialPropertyDto.getType())) { missingFields.add("type"); }
        if (residentialPropertyDto.getFacing() == null) { missingFields.add("facing"); }
        if (!StringUtils.hasText(String.valueOf(residentialPropertyDto.getPropertyListingFor()))) { missingFields.add("propertyListingFor"); }
        if (residentialPropertyDto.getFurnitureStatus() == null) { missingFields.add("furnitureStatus"); }
        if (!StringUtils.hasText(residentialPropertyDto.getDescription())) { missingFields.add("description"); }
        if (residentialPropertyDto.getSuperBuiltUpArea() == null) { missingFields.add("superBuiltUpArea"); }
        if (residentialPropertyDto.getPrice() == null) { missingFields.add("price"); }
        if (residentialPropertyDto.getMoveInDate() == null) { missingFields.add("moveInDate"); }
        if (!StringUtils.hasText(residentialPropertyDto.getPrimaryContact())) { missingFields.add("primaryContact"); }
        if (!StringUtils.hasText(residentialPropertyDto.getMandal())) { missingFields.add("mandal"); }
        if (!StringUtils.hasText(residentialPropertyDto.getVillage())) { missingFields.add("village"); }
        if (!StringUtils.hasText(residentialPropertyDto.getZip())) { missingFields.add("zip"); }
        // Check for valid enum values
        if (residentialPropertyDto.getFacing() != null && !isValidEnumValue(Directions.class, residentialPropertyDto.getFacing().name())) {
            missingFields.add("facing (invalid value)");
        }
        if (residentialPropertyDto.getFurnitureStatus() != null && !isValidEnumValue(Furniture.class, residentialPropertyDto.getFurnitureStatus().name())) {
            missingFields.add("furnitureStatus (invalid value)");
        }
        if (residentialPropertyDto.getPropertyCategory() != null && !isValidEnumValue(PropertyCategory.class, residentialPropertyDto.getPropertyCategory().name())) {
            missingFields.add("propertyCategory (invalid value)");
        }
        if (residentialPropertyDto.getStatus() != null && !isValidEnumValue(PropertyStatus.class, residentialPropertyDto.getStatus().name())) {
            missingFields.add("status (invalid value)");
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