package com.kenaxisq.nestnavigate.property.service.residential;

import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface ResidentialPropertyService {
    Property postResidentialProperty(ResidentialPropertyDto residentialPropertyDto,String userId);
    public Property updateResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId, String propertyId);
}
