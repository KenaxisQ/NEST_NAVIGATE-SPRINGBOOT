package com.kenaxisq.nestnavigate.Media.controller;

import com.kenaxisq.nestnavigate.Media.dto.MediaReadDto;
import com.kenaxisq.nestnavigate.Media.dto.MediaUploadDto;
import com.kenaxisq.nestnavigate.Media.entity.Media;
import com.kenaxisq.nestnavigate.Media.service.MediaService;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<List<Media>>> upload(@ModelAttribute MediaUploadDto mediaUploadDto) throws IOException {
        return ResponseEntity.ok(ResponseBuilder.success(mediaService.uploadFiles(mediaUploadDto),"File Uploaded Successfully"));
    }

    @GetMapping("/read")
    public ResponseEntity<ApiResponse<byte[]>> readFile(@RequestBody MediaReadDto mediaReadDto) throws IOException {
        return ResponseEntity.ok(ResponseBuilder.success(mediaService.readImage(mediaReadDto), "File Retrieved Successfully"));
    }

}
