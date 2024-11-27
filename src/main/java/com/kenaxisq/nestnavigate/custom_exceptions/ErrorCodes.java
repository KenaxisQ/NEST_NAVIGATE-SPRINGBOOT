package com.kenaxisq.nestnavigate.custom_exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    // Authentication & Authorization Errors (AUTH_XXX)
    INVALID_CREDENTIALS("AUTH_001", "Invalid username or password",HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("AUTH_002", "Authentication token has expired",HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("AUTH_003", "Invalid authentication token", HttpStatus.UNAUTHORIZED),
    EMPTY_TOKEN("AUTH_007", "AUTH TOKEN EMPTY", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_PERMISSIONS("AUTH_004", "Insufficient permissions to perform this action", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED("AUTH_005", "Account has been locked", HttpStatus.FORBIDDEN),
    SESSION_EXPIRED("AUTH_006", "Session has expired", HttpStatus.UNAUTHORIZED),
    INVALID_OLD_PASSWORD("AUT_007", "Old password is incorrect", HttpStatus.BAD_REQUEST),
    // User Related Errors (USR_XXX)
    USER_NOT_FOUND("USR_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND),
    USER_NOT_VERIFIED("USR_NOT_VERIFIED", "User not verified, please verify your email", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS("USR_002", "User already exists with this username", HttpStatus.CONFLICT),
    INVALID_USER_DATA("USR_003", "Invalid user data provided", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("USR_004", "Email address already in use", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS("USR_005", "Phone Number already in use", HttpStatus.CONFLICT),
    INVALID_PASSWORD_FORMAT("USR_006", "Password does not meet security requirements", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE("USR_007", "User account is inactive", HttpStatus.FORBIDDEN),
    USER_EMPTY("USR_TABLE_EMPTY", "There are no users currently present in the database.", HttpStatus.OK),

    // Validation Errors (VAL_XXX)
    MISSING_REQUIRED_FIELD("VAL_001", "Required field is missing", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("VAL_002", "Invalid data format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("VAL_003", "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT("VAL_004", "Invalid phone number format", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT("VAL_005", "Invalid date format", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED("VAL_006", "Validation failed", HttpStatus.BAD_REQUEST),

    // Database Operation Errors (DB_XXX)
    DATABASE_ERROR("DB_001", "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_ENTRY("DB_002", "Duplicate entry found", HttpStatus.CONFLICT),
    DATA_INTEGRITY_ERROR("DB_003", "Data integrity violation", HttpStatus.BAD_REQUEST),
    CONNECTION_ERROR("DB_004", "Database connection error", HttpStatus.INTERNAL_SERVER_ERROR),

    // File Operation Errors (FILE_XXX)
    FILE_NOT_FOUND("FILE_001", "File not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_ERROR("FILE_002", "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE("FILE_003", "Invalid file type", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE("FILE_004", "File size exceeds maximum limit", HttpStatus.BAD_REQUEST),

    // System Errors (SYS_XXX)
    INTERNAL_SERVER_ERROR("SYS_001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("SYS_002", "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_API_ERROR("SYS_003", "External API call failed", HttpStatus.BAD_GATEWAY),
    TIMEOUT_ERROR("SYS_004", "Operation timed out", HttpStatus.GATEWAY_TIMEOUT),

    // Property Errors (PRTY_XXX)
    ERR_PROPERTY_NOT_FOUND("PRTY_001", "No Property with the specified ID found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCodes(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
