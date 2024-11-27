package com.kenaxisq.nestnavigate.amenity.controller;

import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.amenity.service.AmenityService;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/amenity")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class AmenityController {

    private AmenityService amenityService;
    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }
    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.getAllAmenities()).getData());
    }
    @GetMapping("id/{id}")
    public ResponseEntity<Amenity> getAmenityFromId(@PathVariable String id) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.getAmenityFromId(id)).getData());
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Amenity> getAmenityFromName(@PathVariable String name) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.getAmenityFromName(name)).getData());
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Amenity>> getAmenityFromCategory(@PathVariable String category) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.getAmenityFromCategory(category)).getData());
    }
    @PostMapping("/create")
    public ResponseEntity<Amenity> createAmenity(@RequestBody Amenity amenity) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.createAmenity(amenity)).getData());
    }
    @PutMapping("/update")
    public ResponseEntity<Amenity> updateAmenity(@RequestBody Amenity amenity) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.updateAmenity(amenity)).getData());
    }
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deleteAmenityFromId(@PathVariable String id) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.deleteAmenityFromId(id)).getData());
    }
    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<String> deleteAmenityFromName(@PathVariable String name) throws Exception{
        return ResponseEntity.ok(ResponseBuilder.success(amenityService.deleteAmenityFromName(name)).getData());
    }
}
