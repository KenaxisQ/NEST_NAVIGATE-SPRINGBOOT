package com.kenaxisq.nestnavigate.property.repository;

import com.kenaxisq.nestnavigate.property.entity.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, String> {
    Property findPropertyById(String id);
}
