package com.kenaxisq.nestnavigate.user.dto;

import lombok.Getter;

@Getter
public class ResetPasswordDto {
    String identifier;
    String OldPassword;
    String NewPassword;
}
