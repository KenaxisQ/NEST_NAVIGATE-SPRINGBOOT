package com.kenaxisq.nestnavigate.property.service.pg;

import com.kenaxisq.nestnavigate.property.dto.LandDto;
import com.kenaxisq.nestnavigate.property.dto.PGDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface PGService {

    Property postPgProperty(PGDto pgDto, String userId);
    Property updatePgProperty(PGDto pgDto, String userId, String propertyId);
}
