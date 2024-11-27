package com.kenaxisq.nestnavigate.amenity.repository;

import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AmenityRepository extends CrudRepository<Amenity, String> {
    Amenity findAmenityByName(String amenityName);
    List<Amenity> findAmenityByCategory(String category);
}
