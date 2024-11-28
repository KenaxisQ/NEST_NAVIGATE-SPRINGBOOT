package com.kenaxisq.nestnavigate.property.service.commercial;

import com.kenaxisq.nestnavigate.property.dto.CommercialPropertyDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface CommercialPropertyService {
    Property postCommercialProperty(CommercialPropertyDto commercialPropertyDto ,String userId);
    Property updateCommercialProperty(CommercialPropertyDto commercialPropertyDto ,String userId, String propertyId);
}
