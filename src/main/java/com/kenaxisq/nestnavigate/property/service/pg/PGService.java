package com.kenaxisq.nestnavigate.property.service.pg;

import com.kenaxisq.nestnavigate.property.entity.Property;

public interface PGService {

    Property postPgProperty(Property pgDto, String userId);
    Property updatePgProperty(Property pgDto, String userId, String propertyId);
}
