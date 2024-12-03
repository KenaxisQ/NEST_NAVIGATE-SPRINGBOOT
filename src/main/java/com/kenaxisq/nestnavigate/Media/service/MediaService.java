package com.kenaxisq.nestnavigate.Media.service;
import com.kenaxisq.nestnavigate.Media.dto.MediaReadDto;
import com.kenaxisq.nestnavigate.Media.dto.MediaUploadDto;
import com.kenaxisq.nestnavigate.Media.entity.Media;

import java.io.IOException;
import java.util.List;


public interface MediaService {
    public List<Media> uploadFiles(MediaUploadDto mediaUploadDto) throws IOException;
    public List<byte[]> readImage(MediaReadDto mediaReadDto);
    public String deleteFile(MediaReadDto mediaReadDto);
}
