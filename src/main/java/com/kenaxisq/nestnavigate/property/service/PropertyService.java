package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PropertyService {
    ResponseEntity<ApiResponse<Property>> getPropertyById(String id);
    Property saveProperty(Property property, String userId);
    ResponseEntity<?> getAllProperties();
    String deleteProperty(String id);
    Property updateProperty(Property property);
}
