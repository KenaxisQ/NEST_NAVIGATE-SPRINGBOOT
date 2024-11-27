package com.kenaxisq.nestnavigate.File.controller;


import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> upload(@RequestParam("media") MultipartFile[] media) {
        try {
            if (media == null)
                throw new ApiException(ErrorCodes.FILE_NOT_FOUND);
            StringBuilder filenames = new StringBuilder();
            Arrays.stream(media).forEach(file -> {
                if (!filenames.isEmpty()) {
                    filenames.append(", ");
                }
                filenames.append(file.getOriginalFilename());
            });

            return ResponseEntity.ok(ResponseBuilder.success(filenames.toString() + " uploaded successfully"));
        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new ApiException("ERR_FILE_UPLOAD","Error while uploading media", HttpStatus.BAD_REQUEST);
        }

    }
}
