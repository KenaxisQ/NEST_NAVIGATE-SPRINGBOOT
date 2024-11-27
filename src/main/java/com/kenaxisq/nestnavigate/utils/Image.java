package com.kenaxisq.nestnavigate.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Image {
    private String imageUrl;
    private String fileName;
    private String description;
    private LocalDateTime uploadDate;

    public Image() {}

    public Image(String imageUrl, String fileName, String description) {
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.description = description;
        this.uploadDate = LocalDateTime.now();
    }

}