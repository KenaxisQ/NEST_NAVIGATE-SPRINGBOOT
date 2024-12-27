package com.kenaxisq.nestnavigate.property.repository;

import com.kenaxisq.nestnavigate.property.dto.AggregatePropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PropertyRepository extends CrudRepository<Property, String> {
    Property findPropertyById(String id);
    List<Property> findAll(Specification<Property> propertiesWithFilters);
}
