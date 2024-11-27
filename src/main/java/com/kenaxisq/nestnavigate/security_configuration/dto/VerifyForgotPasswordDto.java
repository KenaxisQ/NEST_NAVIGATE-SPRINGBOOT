package com.kenaxisq.nestnavigate.security_configuration.dto;

import lombok.Getter;

@Getter
public class VerifyForgotPasswordDto {
    String identifier;
    String code;
    String password;
}
