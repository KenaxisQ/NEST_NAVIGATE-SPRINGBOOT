package com.kenaxisq.nestnavigate.custom_exceptions;

public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}