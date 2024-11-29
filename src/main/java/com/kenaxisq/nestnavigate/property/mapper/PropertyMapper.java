package com.kenaxisq.nestnavigate.property.mapper;

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
