package com.kenaxisq.nestnavigate.Media.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@AllArgsConstructor
@Getter
@Setter
public class ProcessedImage implements AutoCloseable {
    private  InputStream inputStream;
    private  long size;

    @Override
    public void close() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}