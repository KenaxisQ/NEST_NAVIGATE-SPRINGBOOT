package com.kenaxisq.nestnavigate.property_amenities.entity;


import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.property.entity.Property;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PROPERTY_AMENITIES")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyAmenities {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "property_id")
    @ManyToOne
    private Property propertyId;

    @JoinColumn(name = "amenity_id")
    @ManyToOne
    private Amenity amenityId;

    public PropertyAmenities(Property propertyId, Amenity amenityId) {
        this.propertyId = propertyId;
        this.amenityId = amenityId;
    }
}
