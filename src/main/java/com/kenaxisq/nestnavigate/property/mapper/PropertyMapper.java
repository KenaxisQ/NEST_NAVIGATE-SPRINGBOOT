package com.kenaxisq.nestnavigate.property.mapper;

import com.kenaxisq.nestnavigate.property.dto.AggregatePropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;

public class PropertyMapper {

    public static <T, U> U mapDtoToEntity(T dto, Class<U> entityClass) {
        U entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
            Field[] dtoFields = getAllFields(dto.getClass());
            for (Field dtoField : dtoFields) {
                dtoField.setAccessible(true);
                try {
                    Field entityField = entityClass.getDeclaredField(dtoField.getName());
                    entityField.setAccessible(true);
                    entityField.set(entity, dtoField.get(dto));
                } catch (NoSuchFieldException e) {
                    // Log or handle the mismatch, as this means the field does not exist in the entity
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
            // Handle the exception based on your application's requirements
        }
        return entity;
    }
    public static AggregatePropertyDto mapToAggregatePropertyDto(Property property) {
        if (property == null) {
            return null;
        }

        AggregatePropertyDto dto = new AggregatePropertyDto();

        // Mapping fields from Property to AggregatePropertyDto
        dto.setTitle(property.getTitle());
        dto.setType(property.getType());
        dto.setPropertyCategory(property.getPropertyCategory());
        dto.setPropertyListingFor(property.getPropertyListingFor());
        dto.setProjectName(property.getProjectName());
        dto.setDescription(property.getDescription());
        dto.setPrice(property.getPrice());
        dto.setAdvance(property.getAdvance());
        dto.setIsNegotiable(property.getIsNegotiable());
        dto.setPrimaryContact(property.getPrimaryContact());
        dto.setSecondaryContact(property.getSecondaryContact());
        dto.setMandal(property.getMandal());
        dto.setVillage(property.getVillage());
        dto.setZip(property.getZip());
        dto.setAddress(property.getAddress());
        dto.setMedia(property.getMedia());
        dto.setStatus(property.getStatus());
        dto.setIsFeatured(property.getIsFeatured());
        dto.setAmenities(property.getAmenities());

        // Mapping fields specific to Furniture and Directions
        dto.setFurnitureStatus(property.getFurnitureStatus());
        dto.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
        dto.setFacing(property.getFacing());

        // Mapping optional fields with appropriate checks
        dto.setSuperBuiltupArea(property.getSuperBuiltupArea());
        dto.setCarpetArea(property.getCarpetArea());
        dto.setPoojaRoom(property.getPoojaRoom());
        dto.setNoOfBedrooms(property.getNoOfBedrooms());
        dto.setNoOfBathrooms(property.getNoOfBathrooms());
        dto.setNoOfRooms(property.getNoOfRooms());
        dto.setNoOfBalconies(property.getNoOfBalconies());
        dto.setMoveInDate(property.getMoveInDate());

        // Mapping land-specific dimensions
        dto.setLength(property.getLength());
        dto.setWidth(property.getWidth());

        return dto;
    }
//    private static Field[] getAllFields(Class<?> clazz) {
//        Field[] fields = clazz.getDeclaredFields();
//        if (clazz.getSuperclass() != null) {
//            Field[] superClassFields = getAllFields(clazz.getSuperclass());
//            Field[] combinedFields = new Field[fields.length + superClassFields.length];
//            System.arraycopy(fields, 0, combinedFields, 0, fields.length);
//            System.arraycopy(superClassFields, 0, combinedFields, fields.length, superClassFields.length);
//            return combinedFields;
//        }
//        return fields;
//    }
}
