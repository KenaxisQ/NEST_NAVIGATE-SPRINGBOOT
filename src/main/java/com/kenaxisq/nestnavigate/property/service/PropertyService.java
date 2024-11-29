package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.property.dto.AggregatePropertyDto;
import com.kenaxisq.nestnavigate.property.dto.PropertyDto;
import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.filter.dto.PropertyFilterDto;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyService {
    Property getPropertyById(String id);
    Property postProperty(AggregatePropertyDto property, String userId);
    List<Property> getAllProperties();
    String deleteProperty(String id);
    Property updateProperty(Property property);
    public List<Property> searchProperties(PropertyFilterDto filterDto);
}
