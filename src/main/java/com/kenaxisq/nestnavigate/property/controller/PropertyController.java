package com.kenaxisq.nestnavigate.property.controller;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/property")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Property>> getPropertyDetails(@PathVariable String id){
        return propertyService.getPropertyById(id);
    }
        @PostMapping("/create")
        public ResponseEntity<ApiResponse<Property>> createProperty (@RequestBody Property property,@RequestParam String userid) throws ApiException{
        propertyService.saveProperty(property ,userid);
        return ResponseEntity.ok(ResponseBuilder.success(property,"Property Created Successfully"));

        }
        @PutMapping("/update")
        public ResponseEntity<ApiResponse<Property>> updateProperty (@RequestBody Property property) throws ApiException{
        Property propertyToUpdate = propertyService.updateProperty(property);
        return ResponseEntity.ok(ResponseBuilder.success(propertyToUpdate,"Property Updated Successfully"));
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<ApiResponse<String>> deleteProperty (@PathVariable String id) throws ApiException{
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(ResponseBuilder.success(id,"Property Deleted Successfully"));
        }

}
