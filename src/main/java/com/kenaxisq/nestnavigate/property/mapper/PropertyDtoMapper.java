package com.kenaxisq.nestnavigate.property.mapper;

import com.kenaxisq.nestnavigate.property.dto.*;
import com.kenaxisq.nestnavigate.property.entity.Property;

public class PropertyDtoMapper {

    public static PgDto mapToPgDto(AggregatePropertyDto aggDto) {
        PgDto pgDto = new PgDto();
        // Map common properties
        mapCommonProperties(aggDto, pgDto);

        // Map PG-specific properties
        pgDto.setFurnitureStatus(aggDto.getFurnitureStatus());
        pgDto.setFurnitureStatusDescription(aggDto.getFurnitureStatusDescription());

        return pgDto;
    }

    public static ResidentialPropertyDto mapToResidentialPropertyDto(AggregatePropertyDto aggDto) {
        ResidentialPropertyDto residentialDto = new ResidentialPropertyDto();
        // Map common properties
        mapCommonProperties(aggDto, residentialDto);

        // Map Residential-specific properties
        residentialDto.setFacing(aggDto.getFacing());
        residentialDto.setFurnitureStatus(aggDto.getFurnitureStatus());
        residentialDto.setFurnitureStatusDescription(aggDto.getFurnitureStatusDescription());
        residentialDto.setSuperBuiltupArea(aggDto.getSuperBuiltupArea());
        residentialDto.setCarpetArea(aggDto.getCarpetArea());
        residentialDto.setPoojaRoom(aggDto.getPoojaRoom());
        residentialDto.setNoOfBedrooms(aggDto.getNoOfBedrooms());
        residentialDto.setNoOfBathrooms(aggDto.getNoOfBathrooms());
        residentialDto.setNoOfRooms(aggDto.getNoOfRooms());
        residentialDto.setNoOfBalconies(aggDto.getNoOfBalconies());
        residentialDto.setMoveInDate(aggDto.getMoveInDate());
        residentialDto.setLength(aggDto.getLength());
        residentialDto.setWidth(aggDto.getWidth());

        return residentialDto;
    }

    public static CommercialPropertyDto mapToCommercialPropertyDto(AggregatePropertyDto aggDto) {
        CommercialPropertyDto commercialDto = new CommercialPropertyDto();
        // Map common properties
        mapCommonProperties(aggDto, commercialDto);

        // Map Commercial-specific properties
        commercialDto.setFacing(aggDto.getFacing());
        commercialDto.setFurnitureStatus(aggDto.getFurnitureStatus());
        commercialDto.setFurnitureStatusDescription(aggDto.getFurnitureStatusDescription());
        commercialDto.setSuperBuiltupArea(aggDto.getSuperBuiltupArea());
        commercialDto.setCarpetArea(aggDto.getCarpetArea());
        commercialDto.setLength(aggDto.getLength());
        commercialDto.setWidth(aggDto.getWidth());
        commercialDto.setMoveInDate(aggDto.getMoveInDate());

        return commercialDto;
    }

    public static LandDto mapToLandDto(AggregatePropertyDto aggDto) {
        LandDto landDto = new LandDto();
        // Map common properties
        mapCommonProperties(aggDto, landDto);

        // Map Land-specific properties
        landDto.setFacing(aggDto.getFacing());
        landDto.setSuperBuiltupArea(aggDto.getSuperBuiltupArea());
        landDto.setLength(aggDto.getLength());
        landDto.setWidth(aggDto.getWidth());

        return landDto;
    }

    private static void mapCommonProperties(AggregatePropertyDto aggDto, PropertyDto propertyDto) {
        propertyDto.setId(aggDto.getId());
        propertyDto.setTitle(aggDto.getTitle());
        propertyDto.setType(aggDto.getType());
        propertyDto.setPropertyCategory(aggDto.getPropertyCategory());
        propertyDto.setPropertyListingFor(aggDto.getPropertyListingFor());
        propertyDto.setProjectName(aggDto.getProjectName());
        propertyDto.setDescription(aggDto.getDescription());
        propertyDto.setPrice(aggDto.getPrice());
        propertyDto.setAdvance(aggDto.getAdvance());
        propertyDto.setIsNegotiable(aggDto.getIsNegotiable());
        propertyDto.setPrimaryContact(aggDto.getPrimaryContact());
        propertyDto.setSecondaryContact(aggDto.getSecondaryContact());
        propertyDto.setMandal(aggDto.getMandal());
        propertyDto.setVillage(aggDto.getVillage());
        propertyDto.setZip(aggDto.getZip());
        propertyDto.setAddress(aggDto.getAddress());
        propertyDto.setMedia(aggDto.getMedia());
        propertyDto.setStatus(aggDto.getStatus());
        propertyDto.setIsFeatured(aggDto.getIsFeatured());
        propertyDto.setAmenities(aggDto.getAmenities());
    }

        public static PropertyDto mapPropertyToDto(Property property) {
            PropertyDto propertyDto = switch (property.getPropertyCategory().toLowerCase()) {
                case "residential" -> new ResidentialPropertyDto();
                case "commercial" -> new CommercialPropertyDto();
                case "land" -> new LandDto();
                case "pg" -> new PgDto();
                default -> throw new IllegalArgumentException("Unknown property category: " + property.getPropertyCategory());
            };

            return populateToRespectiveDto(property, propertyDto);
        }

        private static <T extends PropertyDto> T populateToRespectiveDto(Property property, T dto) {
            dto.setId(property.getId());
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

            if (dto instanceof ResidentialPropertyDto rDto) {
                rDto.setFacing(property.getFacing());
                rDto.setFurnitureStatus(property.getFurnitureStatus());
                rDto.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
                rDto.setSuperBuiltupArea(property.getSuperBuiltupArea());
                rDto.setCarpetArea(property.getCarpetArea());
                rDto.setLength(property.getLength());
                rDto.setWidth(property.getWidth());
                rDto.setPoojaRoom(property.getPoojaRoom());
                rDto.setNoOfBedrooms(property.getNoOfBedrooms());
                rDto.setNoOfBathrooms(property.getNoOfBathrooms());
                rDto.setNoOfRooms(property.getNoOfRooms());
                rDto.setNoOfBalconies(property.getNoOfBalconies());
                rDto.setMoveInDate(property.getMoveInDate());
            } else if (dto instanceof CommercialPropertyDto cDto) {
                cDto.setFacing(property.getFacing());
                cDto.setFurnitureStatus(property.getFurnitureStatus());
                cDto.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
                cDto.setSuperBuiltupArea(property.getSuperBuiltupArea());
                cDto.setCarpetArea(property.getCarpetArea());
                cDto.setLength(property.getLength());
                cDto.setWidth(property.getWidth());
                cDto.setMoveInDate(property.getMoveInDate());
            } else if (dto instanceof LandDto lDto) {
                lDto.setFacing(property.getFacing());
                lDto.setSuperBuiltupArea(property.getSuperBuiltupArea());
                lDto.setLength(property.getLength());
                lDto.setWidth(property.getWidth());
            } else if (dto instanceof PgDto pDto) {
                pDto.setFurnitureStatus(property.getFurnitureStatus());
                pDto.setFurnitureStatusDescription(property.getFurnitureStatusDescription());
            }

            return dto;
        }
    }