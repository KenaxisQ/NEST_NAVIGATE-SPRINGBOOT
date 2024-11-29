package com.kenaxisq.nestnavigate.property.filter.dto;

import com.kenaxisq.nestnavigate.utils.property.Directions;
import com.kenaxisq.nestnavigate.utils.property.Furniture;
import com.kenaxisq.nestnavigate.utils.property.PropertyStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PropertyFilterDto {
    private String searchKeyword;
    private String type;
    private String propertyCategory;
    private Double minPrice;
    private Double maxPrice;
    private PropertyStatus status;
    private Furniture furnitureStatus;
    private Directions facing;
    private Integer minNoOfBedrooms;
    private LocalDateTime availableFrom;

}