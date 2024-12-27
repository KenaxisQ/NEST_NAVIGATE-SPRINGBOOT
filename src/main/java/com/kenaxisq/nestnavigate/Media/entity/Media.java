package com.kenaxisq.nestnavigate.Media.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Media {


    private String name;

    private String type;

    private String path;

    private String size;

    private String compressedSize;

    private String identifier;

    private String uploadDateTime;
}