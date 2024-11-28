package com.kenaxisq.nestnavigate.property.service.land;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface LandService {
    Property postLandProperty(Property land, String userId);
    Property updateLandProperty(Property land, String userId, String propertyId);
}
