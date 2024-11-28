package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyService {
    Property getPropertyById(String id);
    Property saveProperty(Property property, String userId);
    List<Property> getAllProperties();
    String deleteProperty(String id);
    Property updateProperty(Property property);

}
