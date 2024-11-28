package com.kenaxisq.nestnavigate.property.service.land;

import com.kenaxisq.nestnavigate.property.dto.LandDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface LandService {
    Property postLandProperty(LandDto landDto, String userId);
    Property updateLandProperty(LandDto landDto, String userId, String propertyId);
}
