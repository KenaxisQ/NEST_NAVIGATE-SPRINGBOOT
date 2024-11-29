package com.kenaxisq.nestnavigate.property.mapper;

import com.kenaxisq.nestnavigate.property.dto.*;

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
        landDto.setSuperBuiltUpArea(aggDto.getSuperBuiltupArea());
        landDto.setLength(aggDto.getLength());
        landDto.setWidth(aggDto.getWidth());

        return landDto;
    }

    private static void mapCommonProperties(AggregatePropertyDto aggDto, PropertyDto propertyDto) {
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
    }
}