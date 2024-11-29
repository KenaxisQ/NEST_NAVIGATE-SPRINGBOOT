package com.kenaxisq.nestnavigate.property.dto;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.utils.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LandDto
{
    private Directions facing;
    private Double superBuiltUpArea;
    private Double length;
    private Double width;
}

