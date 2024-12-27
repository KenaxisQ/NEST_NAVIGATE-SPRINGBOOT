package com.kenaxisq.nestnavigate.Media.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MediaUploadDto {
    String identifier;
    Boolean isProperty;
    MultipartFile[] media;
}
