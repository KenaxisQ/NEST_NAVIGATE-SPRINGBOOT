package com.kenaxisq.nestnavigate.property.controller;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.service.PropertyService;
import com.kenaxisq.nestnavigate.property.service.land.LandService;
import com.kenaxisq.nestnavigate.property.service.pg.PGService;
import com.kenaxisq.nestnavigate.property.service.pg.PGServiceImpl;
import com.kenaxisq.nestnavigate.property.validators.PropertyValidator;
import com.kenaxisq.nestnavigate.user.service.UserService;
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
    private final PGService pgService;
    private final LandService landService;
    private final UserService userService;

    @Autowired
    public PropertyController(PropertyService propertyService,
                              PGService pgService,
                              LandService landService,
                              UserService userService) {
        this.propertyService = propertyService;
        this.pgService = pgService;
        this.landService = landService;
        this.userService = userService;
    }
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Property>> getPropertyDetails(@PathVariable String id){
        return ResponseEntity.ok(ResponseBuilder.success(propertyService.getPropertyById(id),"Property Retrieved Successfully"));
        }
        @GetMapping
        public ResponseEntity<ApiResponse<List<Property>>> getAllProperties(){
        List<Property> properties = new ArrayList<>();
        properties = propertyService.getAllProperties();
        return ResponseEntity.ok(ResponseBuilder.success(properties,"Properties Retrieved Successfully"));
        }
        @PostMapping("/create")
        public ResponseEntity<ApiResponse<Property>> createProperty (@RequestBody Property property,@RequestParam String userId) throws ApiException{
        Property propertyToBePosted = new Property();
        property.setListedby(userService.getUser(userId).getAuthorities().toString());
        if(property.getPropertyCategory().equals("PG"))
        {
            PropertyValidator.validatePg(property);
           propertyToBePosted= pgService.postPgProperty(property,userId);

        }
        if(property.getPropertyCategory().equals("LAND"))
        {
                PropertyValidator.validateLand(property);
               propertyToBePosted = landService.postLandProperty(property,userId);

        }
        if(!property.getPropertyCategory().equals("LAND")||!property.getPropertyCategory().equals("PG"))
            {
                return ResponseEntity.ok(ResponseBuilder.success(null,"Property Other than PG, LAND are yet to be implemented"));
            }

//        propertyService.saveProperty(property ,userid);
        return ResponseEntity.ok(ResponseBuilder.success(propertyToBePosted,"Property Created Successfully"));

        }
//        @PutMapping("/update")
//        public ResponseEntity<ApiResponse<Property>> updateProperty (@RequestBody Property property) throws ApiException{
//        Property propertyToUpdate = propertyService.updateProperty(property);
//        return ResponseEntity.ok(ResponseBuilder.success(propertyToUpdate,"Property Updated Successfully"));
//        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<ApiResponse<String>> deleteProperty (@PathVariable String id) throws ApiException{
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(ResponseBuilder.success(id,"Property Deleted Successfully"));
        }

}
