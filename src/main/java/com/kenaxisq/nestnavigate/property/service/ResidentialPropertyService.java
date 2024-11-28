package com.kenaxisq.nestnavigate.property.service;

import com.kenaxisq.nestnavigate.property.dto.ResidentialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface ResidentialPropertyService {
    Property postResidentialProperty(ResidentialPropertyDto residentialPropertyDto,String userId);
    Property putResidentialProperty(ResidentialPropertyDto residentialPropertyDto, String userId);
}
