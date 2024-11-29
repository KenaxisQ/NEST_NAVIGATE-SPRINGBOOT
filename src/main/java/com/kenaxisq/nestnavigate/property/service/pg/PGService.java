package com.kenaxisq.nestnavigate.property.service.pg;

import com.kenaxisq.nestnavigate.property.dto.PgDto;
import com.kenaxisq.nestnavigate.property.entity.Property;

public interface PGService {


    Property postPgProperty(PgDto pg, String userId);

    Property updatePgProperty(Property pgDto, String userId, String propertyId);
}
