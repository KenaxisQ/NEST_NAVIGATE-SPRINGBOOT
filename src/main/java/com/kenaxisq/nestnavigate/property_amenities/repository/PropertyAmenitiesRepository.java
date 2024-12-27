package com.kenaxisq.nestnavigate.property_amenities.repository;
import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property_amenities.entity.PropertyAmenities;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PropertyAmenitiesRepository extends CrudRepository<PropertyAmenities, String> {
    List<PropertyAmenities> findAllByAmenityId(Amenity amenityid);
//    List<Amenity> findAllByProperty(Property property);
    List<PropertyAmenities> findByPropertyIdAndAmenityId(Property property, Amenity amenity);
    PropertyAmenities findPropertyAmenitiesById(String id);
    List<PropertyAmenities> findAllByPropertyId(Property propertyId);
}
